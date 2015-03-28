package wicket.extensions.markup.html.form.wysiwyg;

import java.io.Serializable;

public class WysiwygTextareaSettings implements Serializable
{
    private int width = -1;

    private int height = -1;

    private SelectImagePage selectImagePage;

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public SelectImagePage getSelectImagePage()
    {
        return selectImagePage;
    }

    public void setSelectImagePage(SelectImagePage selectImagePage)
    {
        this.selectImagePage = selectImagePage;
    }
}
