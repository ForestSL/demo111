package com.sl.v0.editors;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.sl.v0.buglocation.BaseFunction;
import com.sl.v0.datas.GlobalVar;

public class Editor extends EditorPart {
    private boolean dirty = false; /* 编辑器是否为脏的标识 初始状态为非脏*/
    private Text text = null;

    /* Editor的初始化方法。本方法前两句是固定不变的*/
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        //System.out.println("init");
        setSite(site);
        setInput(input);
        /* 下一句设置Editor标题栏的显示名称，否则名称用plugin.xml中的name属性*/
        setPartName(GlobalVar.filename);
    }

    /* 在此方法中创建Editor中的界面组件*/
    public void createPartControl(Composite parent) {
        //System.out.println("createPartControl");
        Composite topComp = new Composite(parent, SWT.NONE);
        topComp.setLayout(new FillLayout());
        text = new Text(topComp, SWT.BORDER| SWT.MULTI);
        /*获取文件路径并打开 目前名字为文件全路径*/
        text.setText(new BaseFunction().ReadAllText(GlobalVar.filename));

        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                /* 如果编辑器不脏（即没有修改），则标志它脏并刷新界面状态*/
                if (!isDirty()) {
                    setDirty(true);
                    firePropertyChange(IEditorPart.PROP_DIRTY);
                }
            }
        });
    }

    /* 保存的处理代码在这种方法中，当按Ctrl+S键时会执行此方法。
     *  最后别忘记标志为非脏及刷新界面状态*/
    public void doSave(IProgressMonitor monitor) {
        if (isDirty()) {
            /*更新文件内容*/
        	String content=text.getText();/*获取所有内容*/
        	ArrayList<String> tmp=new ArrayList<String>();/*逐行获取重新写入文件中*/
        	tmp.add(content);
        	(new BaseFunction()).WriteAllLines(GlobalVar.filename, tmp);
        	
            setDirty(false);
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /* 是否允许“另存为”,false不允许*/
    public boolean isSaveAsAllowed() {
        return false;
    }

    /* “另存为”的代码写在这里，本例不实现它*/
    public void doSaveAs() {}

    /* dirty标识的set方法，由此方法设置编辑器为脏*/
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /* 编辑器的内容是否脏了。true脏,false不脏*/
    public boolean isDirty() {
        return dirty;
    }

    /* 当编辑器获得焦点时会执行此方法，本例空实现*/
    public void setFocus() {}
}
