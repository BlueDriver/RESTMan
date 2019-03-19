package rest.component;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import rest.ui.RestManToolWindowFactory;
import rest.ui.RestManToolWindowPanel;
import rest.ui.action.*;
import rest.ui.window.RESTWindow;

import javax.swing.*;

/**
 * RESTMan
 * rest.component
 * RESTMan组件
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/12 16:05 Tuesday
 */
@State(name = "RESTMan", storages = {@Storage(StoragePathMacros.MODULE_FILE)})
public class RestComponent extends AbstractProjectComponent {
    private Project project;

    public RestComponent(Project project) {
        super(project);
        this.project = project;
    }

    public static RestComponent getInstance(@NotNull Project project) {
        return project.getComponent(RestComponent.class);
    }

    /**
     * 初始化组件
     */
    public void initRestComponent(ToolWindow toolWindow, String title) {
        Content content = createContentPanel(toolWindow, title);
        content.setCloseable(true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        ((ToolWindowManagerEx) ToolWindowManager.getInstance(project)).addToolWindowManagerListener(createToolWindowListener());
//        toolWindow.show(new Thread());
        toolWindow.activate(new Thread(), true);

    }

    /**
     * 创建面板界面
     */
    public Content createContentPanel(ToolWindow toolWindow, String title) {
        toolWindow.setToHideOnEmptyContent(true);

        RestManToolWindowPanel panel = new RestManToolWindowPanel(PropertiesComponent.getInstance(project), toolWindow);
        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, title, false);

        RESTWindow RESTWindow = new RESTWindow();

        ActionToolbar toolBar = createToolBar(toolWindow, RESTWindow);

        panel.setToolbar(toolBar.getComponent());
        panel.setContent(RESTWindow.getContent());
        return content;
    }

    /**
     * 创建工具条
     */
    private ActionToolbar createToolBar(ToolWindow toolWindow, RESTWindow RESTWindow) {
        DefaultActionGroup group = new DefaultActionGroup();
        group.addAll(
                new ExecuteAction(toolWindow, project, RESTWindow),
                new AddTabAction(toolWindow, project),
                //todo: 2019-03-19
                //new ShowHistoryAction(toolWindow, project),
                //new HelpAction(toolWindow, project),
                new CloseTabAction(toolWindow, project)
//                new SettingsTabAction()
        );
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, false);
        toolbar.setOrientation(SwingConstants.VERTICAL);
        return toolbar;
    }

    /**
     * 工具栏管理器监听
     */
    private ToolWindowManagerListener createToolWindowListener() {
        return new ToolWindowManagerListener() {
            @Override
            public void toolWindowRegistered(@NotNull String s) {
            }

            @Override
            public void stateChanged() {
                ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(RestManToolWindowFactory.TOOL_ID);
                if (toolWindow != null) {
                    //if no tab, add one
                    if (toolWindow.isVisible() && toolWindow.getContentManager().getContentCount() == 0) {
                        initRestComponent(toolWindow, "Tab1");
                    }
                }
            }
        };
    }

}
