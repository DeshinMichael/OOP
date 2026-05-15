package auto_verification.dsl;

import auto_verification.model.ProjectConfig;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigParser {
    public static ProjectConfig parse(File dslFile) throws IOException {
        // Указ компилятору Groovy использовать мой класс как базу для скриптов
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(DslDelegatingScript.class.getName());

        // Создание оболочки
        GroovyShell shell = new GroovyShell(ConfigParser.class.getClassLoader(), new groovy.lang.Binding(), config);
        DslDelegatingScript script = (DslDelegatingScript) shell.parse(dslFile);

        script.run(); // Запуск DSL-скрипта

        return script.getConfig(); // Возврат заполненной конфигурации
    }
}
