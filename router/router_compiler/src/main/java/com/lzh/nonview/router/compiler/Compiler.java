package com.lzh.nonview.router.compiler;

import com.lzh.nonview.router.anno.RouteConfig;
import com.lzh.nonview.router.anno.RouterRule;
import com.lzh.nonview.router.compiler.exception.RouterException;
import com.lzh.nonview.router.compiler.factory.RuleFactory;
import com.lzh.nonview.router.compiler.model.BasicConfigurations;
import com.lzh.nonview.router.compiler.model.Parser;
import com.lzh.nonview.router.compiler.model.RouteRuleConfig;
import com.lzh.nonview.router.compiler.util.UtilMgr;
import com.lzh.nonview.router.compiler.util.Utils;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * The entry class of annotation processor
 */
public class Compiler extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            BasicConfigurations config = processRouteConfig(roundEnv);
            processRouteRules(roundEnv, config);
            return false;
        } catch (RouterException e) {
            e.printStackTrace();
            error(e.getElement(), e.getMessage());
            return true;
        }
    }

    /**
     * Parse the {@link RouteConfig} and create a {@link BasicConfigurations} to be used.
     *
     * @param roundEnv data sources
     * @return The instance of {@link BasicConfigurations}
     * @throws RouterException pack all of the exception when a error occurs.
     */
    private BasicConfigurations processRouteConfig(RoundEnvironment roundEnv) throws RouterException {
        TypeElement type = null;
        try {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouteConfig.class);
            Iterator<? extends Element> iterator = elements.iterator();
            BasicConfigurations configurations = null;
            while (iterator.hasNext()) {
                type = (TypeElement) iterator.next();
                if (configurations != null) {
                    throw new RouterException("The RouteConfig in this module was defined duplicated!", type);
                }
                if (!Utils.isSuperClass(type, Constants.CLASSNAME_APPLICATION)) {
                    throw new RouterException("The class you are annotated by RouteConfig must be a Application", type);
                }
                RouteConfig config = type.getAnnotation(RouteConfig.class);
                configurations = new BasicConfigurations(config);
            }
            return configurations == null ? new BasicConfigurations(null) : configurations;
        } catch (RouterException e) {
            throw e;
        } catch (Throwable e) {
            throw new RouterException(e.getMessage(), e, type);
        }
    }

    /**
     * 解析由{@link RouterRule}注释的所有元素。并结合{@link BasicConfigurations}来创建新的Java文件
     *
     * @param roundEnv The data sources
     * @param config   The instance of {@link BasicConfigurations} that be parsed by {@link Compiler#processRouteConfig(RoundEnvironment)}
     * @throws RouterException pack all of the exception when a error occurs.
     */
    private void processRouteRules(RoundEnvironment roundEnv, BasicConfigurations config) throws RouterException {
        List<Parser> parsers = new ArrayList<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RouterRule.class);
        if (elements.isEmpty()) {
            return;
        }
        TypeElement type = null;
        try {
            for (Element ele : elements) {
                type = (TypeElement) ele;
                if (!Utils.checkTypeValid(type)) {
                    continue;
                }

                RouterRule rule = type.getAnnotation(RouterRule.class);
                RouteRuleConfig ruleConfig = RouteRuleConfig.create(rule, config, type);

                Parser parser = Parser.create(type, ruleConfig);
                parser.parse();

                parsers.add(parser);
            }

            new RuleFactory(ClassName.get(config.pack, "RouterRuleCreator"), parsers).generateCode();
        } catch (RouterException e) {
            throw e;
        } catch (Throwable e) {
            throw new RouterException(e.getMessage(), e, type);
        }
    }

    /**
     * 发生错误时的编译器输出方法。应该在这里注意。
     *
     * @param element Element of class who has a exception when compiled
     * @param message The message should be noticed to user
     * @param args    args to inflate message
     */
    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(RouterRule.class.getCanonicalName());
        set.add(RouteConfig.class.getCanonicalName());
        return set;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        UtilMgr.getMgr().init(processingEnv);
    }


}
