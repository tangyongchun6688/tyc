package com.ultrapower.ci.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.ultrapower.ci.common.constant.ApplyConstant;

/**
 * @time 2018-03-27
 * @author tangyongchun
 * @description svn地址的处理
 *
 */
public class SvnUtil {

	private static String pomName = "/pom.xml";
	private static SVNRepository repository;

	/**
	 * 初始化操作
	 * 
	 * @throws SVNException
	 */
	private static void initialize(String svnUrl, String svnName, String password) {
		FSRepositoryFactory.setup();
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		try {
			repository = SVNRepositoryFactoryImpl.create(SVNURL.parseURIEncoded(svnUrl));

			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svnName, password);
			repository.setAuthenticationManager(authManager);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提取svn地址及执行命令
	 * 
	 * @param svnUrl
	 * @param svnName
	 * @param password
	 * @return
	 * @throws SVNException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> handleSvnAddress(String svnUrl, String svnName, String password)
			throws SVNException, IOException {
		initialize(svnUrl, svnName, password);
		// 存储pom文件中提取的信息
		Map<String, String> svnMap = new HashMap<String, String>();
		long version = repository.getLatestRevision(); // 获取最新版本号

		try {
			// 获取pom文件存储路径
			String pomPath = ApplyConstant.SVN_POM +  pomName;
			OutputStream outputStream = new FileOutputStream(new File(pomPath));
			// 获取svn的根路径
			SVNURL svnPathRoot = repository.getRepositoryRoot();
			// 获取svn的子路径
			String childPath = (String) svnUrl.substring(svnUrl.indexOf(svnPathRoot.getPath()) + svnPathRoot.getPath().length(), svnUrl.length());
			repository.getFile(childPath + pomName, version, new HashMap(), outputStream);
			outputStream.close();
			// 读取pom临时文件
			svnMap = readXml(pomPath,svnUrl);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return svnMap;
	}

	/**
	 * 读取xml文件
	 * 
	 * @param pomPath pom文件临时存储路径
	 * @param svnUrl svn地址
	 * @return
	 */
	private static Map<String, String> readXml(String pomPath, String svnUrl) {
		Map<String, String> svnMap = new HashMap<String, String>();
		// 读取XML文件，获得document对象
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(pomPath));
			// 获得根节点的属性对象
			Element rootElem = document.getRootElement();
			
			// 获取子应用名
			String childArtifactId = rootElem.element("artifactId").getStringValue();
			// 获取依赖的节点
			Element parentElem = rootElem.element("parent");
			if (parentElem != null) { // 如果父节点不为空，重新设置svn地址及执行命令
				// 获取父应用的svn地址
				String parentSvnPath = svnUrl.substring(0,svnUrl.indexOf(childArtifactId) - 1);
				svnMap.put("svnUrl", parentSvnPath);
				svnMap.put("mvnCmd", "clean package -pl "+ childArtifactId +" -am");
			}else {
				svnMap.put("svnUrl", svnUrl);
				svnMap.put("mvnCmd", "clean package");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		deletePom(pomPath);
		return svnMap;
	}

	/**
	 * 删除临时的pom文件
	 * 
	 * @param pomPath pom文件临时存储路径
	 */
	private static void deletePom(String pomPath) {
		try {
			File file = new File(pomPath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SVNException, IOException {
		String svnUrl = "http://192.168.106.58:57880/svn/ultra-cmdb/5.0/5.0.0/trunk/src/CmdbCluster/cmdb-util";
		Map<String, String> svn = handleSvnAddress(svnUrl, "duyongpeng", "duyongpeng");
		System.out.println(svn);
	}
}
