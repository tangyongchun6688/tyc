package com.ultrapower.ci.common.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;
/**
 * 对资源文件的修改，添加
 * @author mxx
 *
 */
public class creatProperty {
/**
* @param args
*/
//	for循环调用update_properies方法
//	 reuqest.getParameter(“name”)方式获取参数
//	 name可以在定义好的数组里边写
	
/*public static void main(String[] args) {
// TODO Auto-generated method stub
update_properies("Nitconfig","1,666577000,6875,64");
}*/
/**
* 修改/添加job.properties资源文件中键值对;
* 如果K值原先存在则，修改该K值对应的value值;
* 如果K值原先不存在则，添加该键值对到资源中.
* @param key
* @param value
* @author mxx
*/
public static void update_properies(String key,String value, String path){
   //String path =  ;
   File file = new File(path); 
   Properties prop = new Properties(); 
   InputStream inputFile = null;  
        OutputStream outputFile = null;  
        try {  
            inputFile = new FileInputStream(file);//调用这个方法报错
            prop.load(inputFile);  
           // inputFile.close();//一定要在修改值之前关闭inputFile  
            outputFile = new FileOutputStream(file); 
          //设值-保存
            prop.setProperty(key, value); 
            //添加注释
            prop.store(outputFile, "Update");  
        } catch (IOException e) {
       e.printStackTrace();  
       System.out.println("修改配置文件失败");
        }  
        finally{  
            try {  
           if(null!=outputFile){
           outputFile.close();  
           }
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
            try {  
           if(null!=inputFile){
           inputFile.close(); 
           } 
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
 }
public static synchronized String getUUID(){
    UUID uuid=UUID.randomUUID();
    String str = uuid.toString(); 
    String uuidStr=str.replace("-", "");
    return uuidStr;
  }
} 