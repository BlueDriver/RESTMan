package rest.ui.window;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.json.JsonLanguage;
import com.intellij.lang.Language;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import rest.ui.RestManToolWindowFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.TimeUnit;

/**
 * 主窗口面板
 */
public class RESTWindow extends JDialog {
    private JPanel contentPane;
    /**
     * request panel
     */
    private JPanel reqPanel;
    /**
     * request method
     */
    private JComboBox comboBoxMethod;
    /**
     * url textfield
     */
    private JTextField textFieldURL;
    /**
     * request type
     */
    private JRadioButton radioJSONReq;
    private JRadioButton radioXMLReq;
    /**
     * reqeust editor
     */
    private FileEditor reqEditorJSON;
    private FileEditor reqEditorXML;
    /**
     * request content
     */
    private String reqString;
    /**
     * request content is json String?
     * true: json, false: xml
     */
    private boolean isJsonReq = true;

    private JPanel respPanel;
    private JRadioButton radioJSONResp;
    private JRadioButton radioXMLResp;
    private JButton buttonSend;
    private FileEditor respEditorJSON;
    private FileEditor respEditorXML;
    private boolean isJsonResp = true;
    private String respString = "";

    private Project project;

    //获取 application 级别的 PropertiesComponent
    private PropertiesComponent component = PropertiesComponent.getInstance();

    /**
     * init client
     */
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(6_000, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
    private MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
    private MediaType xmlType = MediaType.parse("application/xml; charset=utf-8");

    public JPanel getContent() {
        return contentPane;
    }

    /**
     * post request
     */
    public Response post(String url, String data, MediaType mediaType) throws Exception {
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * get request
     */
    public Response get(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }


    public RESTWindow() {
        setContentPane(contentPane);
        setModal(true);
        //init default value
        textFieldURL.setText(component.getValue("reqUrl", "http://"));
        //req type group
        ButtonGroup group1 = new ButtonGroup();
        group1.add(radioJSONReq);
        group1.add(radioXMLReq);
        group1.setSelected(radioJSONReq.getModel(), true);
        //resp type group
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioJSONResp);
        group2.add(radioXMLResp);
        group2.setSelected(radioJSONResp.getModel(), true);


        class ReqItemListener implements ItemListener {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //System.out.println("req: ");
                if (group1.isSelected(radioJSONReq.getModel())) {
                    //System.out.println("json");
                    reqPanel.remove(reqEditorXML.getComponent());
                    reqPanel.add(reqEditorJSON.getComponent(), BorderLayout.CENTER);
                    isJsonReq = true;
                    //System.out.println(getStringFromEditor(reqEditorJSON));
                } else {
                    //System.out.println("xml");
                    reqPanel.remove(reqEditorJSON.getComponent());
                    reqPanel.add(reqEditorXML.getComponent(), BorderLayout.CENTER);
                    isJsonReq = false;
                    //System.out.println(getStringFromEditor(reqEditorXML));
                }
                reqPanel.updateUI();
            }
        }

        class RespItemListener implements ItemListener {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //System.out.println("resp:");
                if (group2.isSelected(radioJSONResp.getModel())) {
                    //json
                    respPanel.remove(respEditorXML.getComponent());
                    respPanel.add(respEditorJSON.getComponent(), BorderLayout.CENTER);
                    setEditorDoc(respEditorJSON, respString);
                    //System.out.println("json");
                    isJsonResp = true;
                } else {//xml
                    respPanel.remove(respEditorJSON.getComponent());
                    respPanel.add(respEditorXML.getComponent(), BorderLayout.CENTER);
                    setEditorDoc(respEditorXML, respString);
                    isJsonResp = false;
                    // System.out.println("xml");
                }
                respPanel.updateUI();
            }
        }

        radioJSONReq.addItemListener(new ReqItemListener());
        radioXMLReq.addItemListener(new ReqItemListener());

        radioJSONResp.addItemListener(new RespItemListener());
        radioXMLResp.addItemListener(new RespItemListener());

        buttonSend.addActionListener(e -> {
            send();
        });


        project = RestManToolWindowFactory.project;

        reqEditorJSON = createEditor(JsonLanguage.INSTANCE.getID(), null);
        reqEditorXML = createEditor(XMLLanguage.INSTANCE.getID(), null);
        reqPanel.add(reqEditorJSON.getComponent(), BorderLayout.CENTER);

        respEditorJSON = createEditor(JsonLanguage.INSTANCE.getID(), null);
        respEditorXML = createEditor(XMLLanguage.INSTANCE.getID(), null);
        respPanel.add(respEditorJSON.getComponent(), BorderLayout.CENTER);
    }

    /**
     * send reqeust
     */
    public void send() {
        ApplicationManager.getApplication().invokeLater(() -> {
            String url = textFieldURL.getText();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Messages.showErrorDialog("URL must start with http:// or https://", RestManToolWindowFactory.TOOL_ID);
                return;
            }
            new Thread(() -> {
                component.setValue("reqUrl", textFieldURL.getText());
                //设置编辑器loading
                respString = "Loading...";
                setEditorDoc(respEditorJSON, respString);
                setEditorDoc(respEditorXML, respString);
                //提交方式：GET或POST
                String method = comboBoxMethod.getSelectedItem().toString();
                //System.out.println(textFieldURL.getText());
                //System.out.println(method);

                //请求报文类型
                if (isJsonReq) {
                    //System.out.println(getStringFromEditor(reqEditorJSON));
                    reqString = getStringFromEditor(reqEditorJSON);
                } else {
                    //System.out.println(getStringFromEditor(reqEditorXML));
                    reqString = getStringFromEditor(reqEditorXML);
                }
                Response resp;
                try {
                    //get
                    if ("GET".equals(method)) {
                        resp = get(textFieldURL.getText());
                    } else {
                        //post
                        resp = post(textFieldURL.getText(), reqString, isJsonReq == true ? jsonType : xmlType);
                    }
                    respString = resp.body().string();
                } catch (Exception e1) {
                    //e1.printStackTrace();
                    respString = e1.toString();
                }
                //resp
                if (isJsonResp) {
                    //respString = getStringFromEditor(reqEditorJSON);
                    setEditorDoc(respEditorJSON, respString);
                } else {
                    //respString = getStringFromEditor(reqEditorXML);
                    setEditorDoc(respEditorXML, respString);
                }
            }).start();

            //System.out.println("resp: " + respString);
        });
    }

    private void write() {
        //向编辑器写doc
        WriteCommandAction.runWriteCommandAction(project, () -> {
            Document doc = FileDocumentManager.getInstance().getCachedDocument(respEditorJSON.getFile());
            doc.deleteString(0, doc.getTextLength());
            doc.setText("{}");
        });
    }

    /**
     * set content for editor
     */
    private void setEditorDoc(FileEditor editor, String text) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            Document doc = getDocumentFromEditor(editor);
            doc.deleteString(0, doc.getTextLength());
            doc.setText(text == null ? "" : text.replaceAll("\\r", ""));
        });
    }

    /**
     * get content from editor
     */
    private String getStringFromEditor(FileEditor editor) {
        Document doc = getDocumentFromEditor(editor);
        return doc.getText();
    }

    /**
     * get document from editor
     */
    private Document getDocumentFromEditor(FileEditor editor) {
        return FileDocumentManager.getInstance().getCachedDocument(editor.getFile());
    }

    /**
     * create editor with specified language and content
     */
    private FileEditor createEditor(String languageId, String text) {
        if (StringUtils.isEmpty(text)) {
            text = "";
        }
        //Language language = Language.findLanguageByID(JsonLanguage.INSTANCE.getID());
        Language language = Language.findLanguageByID(languageId);
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(language, text);
        FileEditor editor = TextEditorProvider.getInstance().createEditor(project, psiFile.getVirtualFile());
        return editor;
    }


    public static void main(String[] args) {
//        ApplicationManager.getApplication().invokeLater(() -> {
//        });
//        RESTWindow dialog = new RESTWindow();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
    }
}
