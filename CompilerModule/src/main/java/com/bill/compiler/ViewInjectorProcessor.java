package com.bill.compiler;

import com.bill.annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by Bill on 2018/8/17.
 */

@AutoService(Processor.class)
public class ViewInjectorProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Map<TypeElement, List<VariableElement>> map = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(BindView.class.getCanonicalName());
        return set;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        map.clear();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        collectInfo(elements);
        generateCode();

        return true;
    }

    private void collectInfo(Set<? extends Element> elements) {
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

            List<VariableElement> variableElementList = map.get(typeElement);
            if (variableElementList == null) {
                variableElementList = new ArrayList<>();
                map.put(typeElement, variableElementList);
            }
            variableElementList.add(variableElement);
        }
    }

    private void generateCode() {
        for (TypeElement typeElement : map.keySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addParameter(ClassName.get(typeElement.asType()), "activity");

            List<VariableElement> variableElementList = map.get(typeElement);
            for (VariableElement variableElement : variableElementList) {
                String varName = variableElement.getSimpleName().toString();
                String varType = variableElement.asType().toString();
                BindView bindView = variableElement.getAnnotation(BindView.class);
                int params = bindView.value();
                methodBuilder.addStatement("activity.$L = ($L) activity.findViewById($L)", varName, varType, params);
            }

            final String pkgName = getPackageName(typeElement);
            final String clsName = typeElement.getSimpleName().toString() + "$ViewInjector";

            TypeSpec typeSpec = TypeSpec.classBuilder(clsName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();

            JavaFile javaFile = JavaFile.builder(pkgName, typeSpec)
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

}
