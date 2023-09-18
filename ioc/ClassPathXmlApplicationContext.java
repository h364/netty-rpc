package com.hh.netty.nettydemo.dubborpc.ioc;

import com.hh.netty.nettydemo.dubborpc.ioc.beaninterface.BeanFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {
    //存放bean容器的Map
    Map<String,Object> beans = new HashMap<>();

    public ClassPathXmlApplicationContext() throws Exception{
        //使用dom4j解析自己写的beans.xml文件
        SAXReader reader = new SAXReader();
        //指定解析的配置文件
        String path = "beans.xml";
        //获取DOM树
        Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream(path));
        //利用树获取beans.xml的beans根节点
        Element rootElement = document.getRootElement();

        //获取根节点下的所有元素
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            //通过每个bean元素获取到id这个属性，即beanName
            String id = element.attributeValue("id");
            //拿到全限定类名，以便反射调用方法
            String aClass = element.attributeValue("class");

            Class<?> clazz = Class.forName(aClass);
            Object object = clazz.newInstance();

            beans.put(id,object);
            //遍历bean元素下的子元素
            List<Element> sonEl = element.elements();
            for (Element e2 : sonEl) {
                String name = e2.attributeValue("name");
                String refBean = e2.attributeValue("ref");

                String invokeMethod = "set" + name.substring(0,1).toUpperCase()+name.substring(1);

                Object bean = getBean(refBean);

                Method method = object.getClass().getMethod(invokeMethod, bean.getClass().getInterfaces()[0]);
                method.invoke(object,bean);//依赖注入
            }
        }
    }
    @Override
    public Object getBean(String beanName) {
        return this.beans.get(beanName);
    }
}
