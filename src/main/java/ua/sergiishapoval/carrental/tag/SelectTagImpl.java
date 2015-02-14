package ua.sergiishapoval.carrental.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Created by Сергей on 17.12.2014.
 */
public class SelectTagImpl extends BodyTagSupport {
    private String selected;
    private String className;
    private String id;


    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int doStartTag ()throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag ()throws JspException {
        BodyContent bodyContent = getBodyContent();
        if(bodyContent==null){ return SKIP_BODY; }
        StringBuilder stringBuilder = buildSelect(bodyContent);
        try {
            pageContext.getOut().print(stringBuilder.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;
    }

    private StringBuilder buildSelect(BodyContent bodyContent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<select");
        if (className != null){
            stringBuilder.append(" class=\"" + className + "\"");
        }
        if (id != null){
            stringBuilder.append(" id=\"" + id + "\"");
            stringBuilder.append(" name=\"" + id + "\"");
        }
        stringBuilder.append(">");
        String tagBody = bodyContent.getString();
        tagBody= tagBody.replaceFirst(">"+ selected, " selected>" + selected);
        stringBuilder.append(tagBody);
        stringBuilder.append("<select>");
        return stringBuilder;
    }
}