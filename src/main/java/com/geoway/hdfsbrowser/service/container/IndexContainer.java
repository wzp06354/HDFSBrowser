package com.geoway.hdfsbrowser.service.container;

import com.geoway.hdfsbrowser.app.config.AppConfiguration;
import com.geoway.hdfsbrowser.app.view.HNode;
import com.geoway.hdfsbrowser.util.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.TreeItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 2017/4/11.
 */
public class IndexContainer{

    private static final Logger LOGGER=Logger.getLogger(IndexContainer.class);

    private static volatile  IndexContainer CONTAINER=null;

    public static IndexContainer GetContainer()
    {
        if(CONTAINER==null)
        {
            synchronized (IndexContainer.class)
            {
                if(CONTAINER==null)
                {
                    CONTAINER=new IndexContainer();
                }
            }
        }
        return CONTAINER;
    }

    private Map<String,TreeItem> container=null;
    private AppConfiguration configuration=null;
    private IndexContainer()
    {
        container=new HashMap<>();
        configuration=AppConfiguration.GetAppConfiguration();
    }

    public TreeItem[] search(String regex)
    {
        return null;
    }

    public TreeItem get(String path)
    {
        String hdfsPath=null;
        if(!path.startsWith("hdfs://"))
        {
            String prefix="hdfs://"+configuration.getHdfsHost()+":"+configuration.getHdfsPort();
            if(!path.startsWith("/"))
            {
                prefix=prefix+"/";
            }
            hdfsPath=prefix+path;
        }
        else
        {
            hdfsPath=path;
        }
        hdfsPath=FileUtils.CorrectPath(hdfsPath);
        //
        if(container.containsKey(hdfsPath))
        {
            return container.get(hdfsPath);
        }
        return null;
    }

    public synchronized void addIndex(TreeItem item)
    {
        if(item==null)
        {
            LOGGER.info("item is null");
            return;
        }
        HNode node= (HNode) item.getData();
        if(node==null)
        {
            LOGGER.info("the node is null");
            return;
        }
        String key=node.getPath();
        if(key==null || key.isEmpty())
        {
            LOGGER.info("the key is null");
            return;
        }
        //make sure that the key is not end with char /
        key= FileUtils.CorrectPath(key);
        container.put(key,item);
    }

    public void close()
    {
        container.clear();
        System.gc();
    }
}
