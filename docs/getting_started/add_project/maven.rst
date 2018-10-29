Maven зависимости
=================

Для подключения проекта необходимо в первую очередь добавить в ``pom.xml`` репозиторий

.. code:: xml

    <repositories>
        <repository>
            <id>touchbit</id>
            <url>https://touchbit.org/repository/</url>
        </repository>
    </repositories>

Далее есть 2 пути развития событий, простой и правильный.

Простое подключение проекта
---------------------------

Для уменьшения головной боли при создании нового проекта с тестами, рекомендуется унаследоваться от корневого ``pom.xml`` Buggy.

Для этого необходимо добавить в ``pom.xml`` на уровне ``<project>`` родителя:

.. code:: xml

    <parent>
        <groupId>org.touchbit.buggy</groupId>
        <artifactId>buggy</artifactId>
        <version>?.?.?</version>
    </parent>

Это нам даёт следующие приемущества:

#. Версия gdfgdggdfgdggdfgdg