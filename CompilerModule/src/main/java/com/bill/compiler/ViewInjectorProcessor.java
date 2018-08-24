package com.bill.compiler;

import com.bill.annotation.BindView;
import com.bill.annotation.OnClick;
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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
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
    private Map<TypeElement, List<Element>> map = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(BindView.class.getCanonicalName());
        set.add(OnClick.class.getCanonicalName());
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

        Set<? extends Element> bindViewSet = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> onClickSet = roundEnvironment.getElementsAnnotatedWith(OnClick.class);

        collectInfo(bindViewSet);
        collectInfo(onClickSet);
        generateCode();

        return true;
    }

    private void collectInfo(Set<? extends Element> elements) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            List<Element> elementList = map.get(typeElement);
            if (elementList == null) {
                elementList = new ArrayList<>();
                map.put(typeElement, elementList);
            }
            elementList.add(element);
        }
    }

    private void generateCode() {
        for (TypeElement typeElement : map.keySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(typeElement.asType()), "target", Modifier.FINAL)
                    .addParameter(ClassName.get("android.view", "View"), "view");

            List<Element> elements = map.get(typeElement);
            for (Element element : elements) {
                ElementKind kind = element.getKind();
                if (kind == ElementKind.FIELD) {
                    VariableElement variableElement = (VariableElement) element;
                    String varName = variableElement.getSimpleName().toString();
                    String varType = variableElement.asType().toString();
                    BindView bindView = variableElement.getAnnotation(BindView.class);
                    int params = bindView.value();
                    methodBuilder.addStatement("target.$L = ($L) view.findViewById($L)", varName, varType, params);
                } else if (kind == ElementKind.METHOD) {
                    ExecutableElement executableElement = (ExecutableElement) element;
                    OnClick clickView = executableElement.getAnnotation(OnClick.class);
                    int params = clickView.value();
                    methodBuilder.addStatement("android.view.View cView = (android.view.View) view.findViewById($L)", params);
                    MethodSpec innerMethodSpec = MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(ClassName.get("android.view", "View"), "v")
                            .addStatement("target.$L()", executableElement.getSimpleName().toString())
                            .build();
                    TypeSpec innerTypeSpec = TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(ClassName.bestGuess("View.OnClickListener"))
                            .addMethod(innerMethodSpec)
                            .build();
                    methodBuilder.addStatement("cView.setOnClickListener($L)", innerTypeSpec);
                }
            }

            final String pkgName = getPackageName(typeElement);
//            final String clsName = typeElement.getSimpleName().toString() + "$ViewInjector";
            final String clsName = getClassName(typeElement, pkgName) + "$ViewInjector";

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

    private String getClassName(TypeElement type, String pkgName) {
        int packageLength = pkgName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLength).replace('.', '$');
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

}
