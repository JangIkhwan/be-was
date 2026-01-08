package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelAndViewImpl implements ModelAndView{
    private Map<String, String> model = new HashMap<>();
    private String viewName;
    Boolean isDynamic = false;
    Boolean isRedirect = false;

    public static ModelAndViewImpl forward(String viewName) {
        ModelAndViewImpl mav = new ModelAndViewImpl();
        mav.viewName = viewName;
        return mav;
    }

    public static ModelAndViewImpl redirect(String viewName) {
        ModelAndViewImpl mav = new ModelAndViewImpl();
        mav.viewName = viewName;
        mav.isRedirect = true;
        return mav;
    }

    public static ModelAndViewImpl forwardDynamic(String viewName) {
        ModelAndViewImpl mav = new ModelAndViewImpl();
        mav.viewName = viewName;
        mav.isDynamic = true;
        return mav;
    }

    @Override
    public ModelAndViewImpl addModelAttribute(String name, String value) {
        model.put(name, value);
        return this;
    }

    @Override
    public Boolean isRedirect(){
        return this.isRedirect;
    }

    @Override
    public Boolean isDynamic(){
        return this.isDynamic;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public Set<String> getModelNames() {
        return model.keySet();
    }

    @Override
    public String getModelAttribute(String name) {
        return model.get(name);
    }
}
