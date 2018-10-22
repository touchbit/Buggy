package org.touchbit.buggy.core.config;

import org.touchbit.buggy.core.process.Component;
import org.touchbit.buggy.core.process.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleg Shaburov on 16.09.2018
 * shaburov.o.a@gmail.com
 */
public class TestComponent extends Component {

    @Override
    public List<Service> getServices() {
        return new ArrayList<Service>() {{add(new TestService());}};
    }

    @Override
    public String getDescription() {
        return "TestComponent";
    }

}