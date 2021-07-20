package org.subieslaw.finance.architecture;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption;

@AnalyzeClasses(packages = "org.subieslaw.finance", importOptions = { ImportOption.DoNotIncludeTests.class })
public class NamingConventionsTest {
    
    @ArchTest
    static ArchRule controllers_should_not_have_Gui_in_name =
            classes()
                    .that().resideInAPackage("..demo..")
                    .should().haveSimpleNameNotContaining("Demo");

}
