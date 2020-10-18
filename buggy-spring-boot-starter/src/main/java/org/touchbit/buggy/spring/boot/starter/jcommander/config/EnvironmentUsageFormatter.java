package org.touchbit.buggy.spring.boot.starter.jcommander.config;

import com.beust.jcommander.*;
import org.touchbit.buggy.core.goal.component.Component;
import org.touchbit.buggy.core.goal.interfaze.Interface;
import org.touchbit.buggy.core.goal.service.Service;
import org.touchbit.buggy.core.utils.ANSI;
import org.touchbit.buggy.spring.boot.starter.util.BeanScanner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.touchbit.buggy.core.utils.ANSI.BOLD;

/**
 * Created by Oleg Shaburov on 18.10.2020
 * shaburov.o.a@gmail.com
 */
public class EnvironmentUsageFormatter extends DefaultUsageFormatter {

    public EnvironmentUsageFormatter(JCommander commander) {
        super(commander);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void appendAllParametersDetails(StringBuilder out,
                                           int indentCount,
                                           String indent,
                                           List<ParameterDescription> sortedParameters) {
        if (sortedParameters.size() > 0) {
            out.append(indent).append("  Options:\n");
        }
        for (ParameterDescription pd : sortedParameters) {
            WrappedParameter parameter = pd.getParameter();
            String description = pd.getDescription();
            boolean hasDescription = !description.isEmpty();

            String longestName = pd.getLongestName();
            while (longestName.startsWith("-")) {
                longestName = longestName.replaceFirst("-", "");
            }
            String envKey = "BUGGY_" + longestName.replace("-", "_").toUpperCase();
            // First line, command name
            out.append(indent)
                    .append("  ")
                    .append(parameter.required() ? "* " : "  ")
                    .append(BOLD.wrap(pd.getNames() + " | " + "${" + envKey + "}"))
                    .append("\n");

            if (hasDescription) {
                wrapDescription(out, indentCount, s(indentCount) + description);
            }
            Object def = pd.getDefault();

            if (pd.isDynamicParameter()) {
                String syntax = "Syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value";

                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(syntax);
            }

            if (def != null && !pd.isHelp()) {
                String displayedDef = Strings.isStringEmpty(def.toString()) ? "<empty string>" : def.toString();
                String defaultText = ANSI.BR_PURPLE.wrap("Default: ") +
                        (parameter.password() ? "********" : displayedDef);
                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(defaultText);
            }
            Parameterized parameterized = pd.getParameterized();
            Class<?> type = parameterized.getType();
            if (type.equals(List.class)) {
                Type listType = parameterized.getGenericType();
                if (listType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) listType;
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    String genericTypeName = genericType.getTypeName();
                    StringJoiner sj = new StringJoiner(", ");
                    if (Component.class.getTypeName().equals(genericTypeName)) {
                        Set<Component> components = BeanScanner.getBeanDefinitionInstances(Component.class);
                        components.forEach(c -> sj.add(c.getName()));
                    }
                    if (Service.class.getTypeName().equals(genericTypeName)) {
                        Set<Service> components = BeanScanner.getBeanDefinitionInstances(Service.class);
                        components.forEach(c -> sj.add(c.getName()));
                    }
                    if (Interface.class.getTypeName().equals(genericTypeName)) {
                        Set<Interface> components = BeanScanner.getBeanDefinitionInstances(Interface.class);
                        components.forEach(c -> sj.add(c.getName()));
                    }
                    if (org.touchbit.buggy.core.model.Type.class.getTypeName().equals(genericTypeName)) {
                        Arrays.stream(org.touchbit.buggy.core.model.Type.values())
                                .forEach(c -> sj.add(c.name()));
                    }
                    String possibleValues = ANSI.BR_BLUE.wrap("Possible Values:") + " [" + sj.toString() + "]";
                    out.append("\n");
                    wrapDescription(out, indentCount, s(indentCount) + possibleValues);
                }
            }
            if (type.isEnum()) {
                String valueList = EnumSet.allOf((Class<? extends Enum>) type).toString();
                String possibleValues = ANSI.BR_BLUE.wrap("Possible Values: ") + valueList;

                // Prevent duplicate values list, since it is set as 'Options: [values]' if the description
                // of an enum field is empty in ParameterDescription#init(..)
                if (!description.contains("Options: " + valueList)) {
                    if (hasDescription) {
                        out.append(newLineAndIndent(indentCount));
                    } else {
                        out.append(s(indentCount));
                    }
                    out.append(possibleValues);
                }
            }
            out.append("\n");
        }
    }

    /**
     * Returns new line followed by indent-many spaces.
     *
     * @return new line followed by indent-many spaces
     */
    private static String newLineAndIndent(int indent) {
        return "\n" + s(indent);
    }

}
