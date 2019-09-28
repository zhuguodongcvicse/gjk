package com.inforbus.gjk.common.core.jgit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import lombok.Getter;

/**
 * Git库操作工具类
 * 
 * @author dong
 *
 */
public class JGitUtil {

	@Getter
	private static String LOCAL_REPO_PATH;
	@Getter
	private static String LOCAL_REPOGIT_CONFIG;
	@Getter
	private static String REMOTE_REPO_URI;
	@Getter
	private static String GIT_USERNAME;
	@Getter
	private static String GIT_PASSWORD;

	private static String INIT_LOCAL_CODE_DIR;
	private static String LOCAL_CODE_CT_SQL_DIR;
	private static String BRANCH_NAME;
	final static Logger log = LoggerFactory.getLogger(JGitUtil.class);

	static {
		// 获取当前类的路径
		String filePath = JGitUtil.class.getResource("").getPath();
		try {
			// 中文乱码问题
			filePath = URLDecoder.decode(filePath, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 找到bootstrap.properties的地址
		filePath = filePath.substring(0, filePath.indexOf("target/classes/") + "target/classes/".length())
				+ "bootstrap.properties";
		File dumpFile = new File(filePath);

		Map father;
		try {
			father = Yaml.loadType(dumpFile, HashMap.class);
			LOCAL_REPO_PATH = father.get("git.local.path").toString();
			LOCAL_REPOGIT_CONFIG = father.get("git.local.config").toString();
			REMOTE_REPO_URI = father.get("git.remote.uri").toString();
			GIT_USERNAME = father.get("git.username").toString();
			GIT_PASSWORD = father.get("git.password").toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// writeFileToGit(SqlTypeEnum.EMAIL, "admin", "-- 测试hehe \n select * from
		// dual;", "test_测试_007", "test7");
		// System.out.println(commitAndPush("gjk/test1/test.txt", "test3"));
		// File dumpFile = new File(System.getProperty("user.dir") +
		// "/src/main/resources/bootstrap.properties");
		// try {
		// Map father = Yaml.loadType(dumpFile, HashMap.class);
		// for (Object key : father.keySet()) {
		// System.out.println(key + ":\t" + father.get(key).toString());
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		System.err.println(LOCAL_REPO_PATH);
		System.err.println(LOCAL_REPOGIT_CONFIG);
		System.err.println(REMOTE_REPO_URI);
		System.err.println(GIT_USERNAME);
		System.err.println(GIT_PASSWORD);
		// pullNewBranchToLocal("ssh://admin@192.168.2.142:29418/gjk.git","test");
	}

	/**
	 * 提交并推送代码至远程服务器
	 * 
	 * @param filePath 提交文件路径(相对路径,如 gjk/test/test3.txt)
	 * @param desc     提交描述
	 * @return 默认返回版本号；为-1时表示异常；为0时表示要提交的文件没有被修改，不能提交
	 */
	public static String commitAndPush(String filePath, String desc) {

		String commitAndPushFlag = "-1";
//		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
//			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(GIT_USERNAME,
//					GIT_PASSWORD);
//
//			// 判断是否有被修改过的文件
//			List<DiffEntry> diffEntries = git.diff().setPathFilter(PathFilterGroup.createFromStrings(filePath))
//					.setShowNameAndStatusOnly(true).call();
//			if (diffEntries == null || diffEntries.size() == 0) {
//				// "提交的文件内容都没有被修改，不能提交"
//				commitAndPushFlag = "0";
//				return commitAndPushFlag;
//			}
//
//			git.add().addFilepattern(filePath).call();
//
//			// 提交
//			RevCommit rev = git.commit().setMessage(desc).call();
//
//			// 返回版本号
//			commitAndPushFlag = rev.getName();
//
//			// 推送到远程
//			if (StringUtils.isNotBlank(GIT_USERNAME) || StringUtils.isNotBlank(GIT_PASSWORD)) {
//				git.push().setRemote("origin").setCredentialsProvider(provider).call();
//			} else {
//				git.push().call();
//			}
//			log.info("Commit And Push file " + filePath + " to repository at " + git.getRepository().getDirectory());
//		} catch (Exception e) {
//			e.printStackTrace();
//			commitAndPushFlag = "-1";
//			log.error("Commit And Push error! \n" + e.getMessage());
//		}
		return commitAndPushFlag;

	}

	/**
	 * 添加文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean addFile(String fileName) {

		boolean addFileFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			String filePath = LOCAL_CODE_CT_SQL_DIR + fileName;
			git.add().addFilepattern(INIT_LOCAL_CODE_DIR).call();
			System.out.println("Added file " + filePath + " to repository at " + git.getRepository().getDirectory());
		} catch (Exception e) {
			e.printStackTrace();
			addFileFlag = false;
		}
		return addFileFlag;
	}

	/**
	 * 提交代码到本地仓库
	 * 
	 * @param filePath 文件位置(相对仓库位置:a/b/file)
	 * @param comment  提交git内容描述
	 * @return
	 */
	public static boolean commitFile(String comment) {

		boolean commitFileFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			// 提交代码到本地仓库
			git.commit().setMessage(comment).call();
			log.info("Committed to repository at " + git.getRepository().getDirectory());
		} catch (Exception e) {
			e.printStackTrace();
			commitFileFlag = false;
			log.error("commitFile error! \n" + e.getMessage());
		}
		return commitFileFlag;
	}

	/**
	 * 将文件上传到远程库
	 * 
	 * @return
	 */
	public static boolean push() {
		boolean pushFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			// 提交代码到本地仓库
			git.push().call();
			log.info("push " + git.getRepository() + File.separator + git.getRepository().getBranch());
		} catch (Exception e) {
			e.printStackTrace();
			pushFlag = false;
			log.error("push error! \n" + e.getMessage());
		}
		return pushFlag;
	}

	/**
	 * 拉取远程代码
	 * 
	 * @param remoteBranchName
	 * @return 远程分支名
	 */
	public static boolean pull() {
		return pull(BRANCH_NAME);
	}

	public static boolean pull(String remoteBranchName) {

		boolean pullFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(GIT_USERNAME,
					GIT_PASSWORD);
			git.pull().setRemoteBranchName(remoteBranchName).setCredentialsProvider(provider).call();
		} catch (Exception e) {
			e.printStackTrace();
			pullFlag = false;
		}
		return pullFlag;
	}

	public static boolean checkout(String branchName) {

		boolean checkoutFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			git.checkout().setName("refs/heads/" + branchName).setForce(true).call();
		} catch (Exception e) {
			e.printStackTrace();
			checkoutFlag = false;
		}
		return checkoutFlag;
	}

	public static boolean checkout() {
		return checkout(BRANCH_NAME);
	}

	/**
	 * 从远程获取最新版本到本地 不会自动合并 merge
	 * 
	 * @param branchName
	 * @return
	 */
	public static boolean fetch() {

		boolean fetchFlag = true;
		try (Git git = Git.open(new File(LOCAL_REPOGIT_CONFIG));) {
			git.fetch().setCheckFetchedObjects(true).call();
		} catch (Exception e) {
			e.printStackTrace();
			fetchFlag = false;
		}
		return fetchFlag;
	}

	/**
	 * 拉取新创建的分支到本地
	 * 
	 * @param cloneURL
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static boolean pullNewBranchToLocal(String cloneURL, String branchName) {
		boolean resultFlag = false;
		// String[] splitURL = cloneURL.split(" ");
		// String branchName = splitURL[1];
		String fileDir = INIT_LOCAL_CODE_DIR + "/" + branchName;
		// 检查目标文件夹是否存在
		File file = new File(fileDir);
		if (file.exists()) {
			deleteFolder(file);
		}
		Git git;
		try {
			git = Git.open(new File(LOCAL_REPOGIT_CONFIG));
			// 设置远程服务器上的用户名和密码
			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(GIT_USERNAME,
					GIT_PASSWORD);
			git.cloneRepository().setURI(cloneURL).setDirectory(file).setCredentialsProvider(provider).call();
			resultFlag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return resultFlag;
	}

	/**
	 * 删除指定文件夹
	 * 
	 * @param file
	 */
	private static void deleteFolder(File file) {
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFolder(files[i]);
				files[i].delete();
			}
		}
	}

	/**
	 * 生成文件写内容
	 * 
	 * @param content  文件内容
	 * @param filePath 文件名称
	 */
	private static boolean createFile(String content, String filePath) {
		boolean createFileFlag = true;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				createFileFlag = false;
			}
		}
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8));) {
			bw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			createFileFlag = false;
		} catch (IOException e) {
			e.printStackTrace();
			createFileFlag = false;
		}
		return createFileFlag;
	}

	/**
	 * 创建本地新仓库
	 * 
	 * @param repoPath 仓库地址 D:/workspace/TestGitRepository
	 * @return
	 * @throws IOException
	 */
	public static Repository createNewRepository(String repoPath) throws IOException {
		File localPath = new File(repoPath);
		// create the directory
		Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
		repository.create();
		return repository;
	}

	/**
	 * 创建仓库，仅需要执行一次
	 */
	public static boolean setupRepository() {
		boolean setupRepositoryFlag = true;
		try {
			// 设置远程服务器上的用户名和密码
			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(GIT_USERNAME,
					GIT_PASSWORD);
			if (StringUtils.isBlank(GIT_USERNAME) && StringUtils.isBlank(GIT_PASSWORD)) {
				Git git = Git.cloneRepository().setURI(REMOTE_REPO_URI) // 设置远程URI
						.setBranch("master") // 设置clone下来的分支,默认master
						.setDirectory(new File(LOCAL_REPO_PATH)) // 设置下载存放路径
						.call();
			} else {
				Git git = Git.cloneRepository().setURI(REMOTE_REPO_URI) // 设置远程URI
						.setBranch("master") // 设置clone下来的分支,默认master
						.setDirectory(new File(LOCAL_REPO_PATH)) // 设置下载存放路径
						.setCredentialsProvider(provider) // 设置权限验证
						.call();
			}
		} catch (Exception e) {
			e.printStackTrace();
			setupRepositoryFlag = false;
		}
		return setupRepositoryFlag;
	}

}