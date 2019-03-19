package rest.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;

/**
 * RESTMan
 * rest.ui
 * 工具面板
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 15:59 Tuesday
 */
public class RestManToolWindowPanel extends SimpleToolWindowPanel {
    private PropertiesComponent myPropertiesComponent;
    private ToolWindow myWindow;

    public RestManToolWindowPanel(PropertiesComponent myPropertiesComponent, ToolWindow myWindow) {
        super(false, true);
        this.myPropertiesComponent = myPropertiesComponent;
        this.myWindow = myWindow;
    }

}
