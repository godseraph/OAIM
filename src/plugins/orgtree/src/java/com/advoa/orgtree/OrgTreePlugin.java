package com.advoa.orgtree;

import java.net.URL;

import javax.swing.ImageIcon;

import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;

//展示OA的组织结构
public class OrgTreePlugin implements Plugin{

    private static ImageIcon  organ_icon=null;
    
    public static ImageIcon    getOrganIcon(){//机构图标
        if(organ_icon==null){
            ClassLoader  cl=OrgTreePlugin.class.getClassLoader();
            System.out.println(System.getProperty("user.dir" ));
            URL   imageURL=cl.getResource("images/organ.gif");
            System.out.println("imagepath:"+imageURL.getPath());
            organ_icon=new   ImageIcon(imageURL);
        }
        return  organ_icon;
    }
    
    private static ImageIcon  user_icon=null;
    
    public          static    ImageIcon     getUserIcon(){//机构图标
        if(user_icon==null){
            ClassLoader    cl=OrgTreePlugin.class.getClassLoader();
            URL  imageURL=cl.getResource("images/user.gif");
            user_icon=new   ImageIcon(imageURL);
        }
        return user_icon;
    }
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        Workspace  workspace=SparkManager.getWorkspace();

        SparkTabbedPane tabbedPane=workspace.getWorkspacePane();

        OrgTree   orgTreePanel=new OrgTree();//机构树

        
        
        // Add own Tab.
        tabbedPane.addTab("组织架构",OrgTreePlugin.getOrganIcon(),orgTreePanel);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    // 是否可关闭
    @Override
    public boolean canShutDown() {
        // TODO Auto-generated method stub
        return false;
    }

    // 卸载
    @Override
    public void uninstall() {
        // TODO Auto-generated method stub

    }

}