package com.tmt.similarity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.tmt.util.MyStaticValue;

/**
 * 文件名：WordSimilarity.java
 * 类描述：词相似度（词形，词序，词音，词性，词义）
 * 作者：ywc
 * 创建时间：2015年4月24日
 * 修改人：
 * 修改备注：
 */
public class WordSimilarity implements SimilarityInterf{

	//形近字路径
//	public String SIMILAR_PATH = System.getProperty("user.dir") + "/dict/形近字.txt";
	
	//形近字字典
	public Map<String,HashSet<String>> similarMap = new HashMap<String, HashSet<String>>(); 
	
	public String s1 = "";
	public String s2 = "";
	
	//字形相似度
	public double PatternSim;
	//字序相似度
	public double SequenceSim;
	//读音相似度
	public double PinyinSim; 
	//语义相似度
	public double SemanticSim; 
	
	public WordSimilarity(){
		super();
		PatternSim  = 0.0;
		SequenceSim = 0.0;
		PinyinSim   = 0.0;
		SemanticSim = 0.0;
		//MakeSimilarCharDic();	
	}
	
	public WordSimilarity(String s1,String s2){
		this.s1     = s1;
		this.s2     = s2;
		PatternSim  = 0.0;
		SequenceSim = 0.0;
		PinyinSim   = 0.0;
		SemanticSim = 0.0;
		MakeSimilarCharDic();
	}
	
	//加载形近字字典
	public void MakeSimilarCharDic() {

        BufferedReader br;
		try {
			// 系统分隔符
			String FILE_SEPARATOR = System.getProperty("file.separator");
			// 读取形近字字典
			br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("dict"+FILE_SEPARATOR+"形近字.txt"),"utf-8"));				
	        String temp = null;
	        while((temp = br.readLine()) != null){
	        	String[] strs = temp.split(","); 	        	
	        	for (int i = 0; i < strs.length; i++) {
	        		String str = strs[i];
	        		if(str.equals("   ")){
	        			continue;
	        		}
	        		str = str.trim();
	    			if(similarMap.keySet().contains(str)){    				
	    				for (int j = 0; j < strs.length; j++) {
	    					String str2= strs[j];
	    					if (str2.equals(str)) {
								continue;
							}
	    					str2.trim();
	    					similarMap.get(str).add(str2);
						}
	    			}else {
	    				HashSet<String> set = new HashSet<String>();
	       				for (int j = 0; j < strs.length; j++) {
	    					String str2= strs[j];
	    					if (str2.equals(str)) {
								continue;
							}
	    					str2.trim();
	       					set.add(str2);
						}       				
	       				similarMap.put(str,set);
					}    			
				}	
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.print("similar size :" + similarMap.size()+"\n");
	}

	//查询两个字相似
	public boolean IsSimilar(char c1,char c2){
		if (similarMap.keySet().contains(String.valueOf(c1))) {
			if(similarMap.get(String.valueOf(c1)).contains(String.valueOf(c2))){
				return true;
			}
		}
		return false;
	}
	
	//计算词形相似度
	public double PatternSimilar(){
		//计算两句话中相同单词的个数
		int sameCount=0;	
		int similarCount = 0;
		String temp = s2;
		char[] chars1 = s1.toCharArray();
		if(s1.equals(s2)){
			sameCount = s1.length();
			similarCount = 0 ;
		}else{			
			for (int i = 0; i < chars1.length; i++) {
				char c=chars1[i];
				//排除重字
				if(s1.substring(0, i).contains(String.valueOf(c))){
					continue;
				}
				if (temp.contains(String.valueOf(c))) {
					sameCount++;
					temp = temp.replace(String.valueOf(c), "");
				}else {
					char[] chars2 = temp.toCharArray();		
					for (int j = 0; j < chars2.length; j++) {
						if (IsSimilar(c,chars2[j])) {
							similarCount++;
							break;
						}
					}
				}
			}

		}		
		double sameSim=2*sameCount*1.0/(s1.length()+s2.length());
		double similarSim=2*similarCount*0.5/(s1.length()+s2.length());
		PatternSim = sameSim+similarSim;
		return PatternSim;
	}
	
	//判断一个字在A中的位置序号
	public int getSequenceNo(char[] chars,char c){
		int sequenceNo=-1;
		for(int i=0;i<chars.length;i++){
			if(chars[i] == c){
				sequenceNo=i;//找到序号
				break;//退出循环
			}
		}
		//返回结果
		return sequenceNo;
	}
	
	//获取词序相似度
	public  double SequenceSimilar(){
		char[] chars1 = s1.toCharArray();
		char[] chars2 = s2.toCharArray();
		
		//S2词在S1中位置   
		int count=0;
		for(int i=0;i<chars2.length;i++){
			char one=chars2[i];
			int pos = getSequenceNo(chars1,one);
			if(pos ==  i){ 
				count ++;
			}
		}
		  
		SequenceSim = 2*count*1.0/(s1.length()+s2.length());
		return SequenceSim;
	}
	
	//汉语转拼音
	public HashSet<String> HanConvertPinYin(char c){	
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
	    format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE); 
	    String[] pinYin  = PinyinHelper.toHanyuPinyinStringArray(c);
	    HashSet<String> set = new HashSet<String>();
 	    //去拼音音调
	    for (String string : pinYin) {
	    	String str = string.substring(0, string.length()-1);
	    	set.add(str);
		}
		return set;
	}
	
	//获取读音相似度
	public double PinYinSimilar(){
		char[] chars1 = s1.toCharArray();
		char[] chars2 = s2.toCharArray();
		
		int count = 0;
		//S2词在S1中位置   
		for(int i=0;i<chars1.length && i<chars2.length ;i++){ 
			Set<String> pinyin1 = HanConvertPinYin(chars1[i]);
//			//排除重字
//			if(s1.substring(0, i).contains(String.valueOf(chars1[i]))){
//				continue;
//			} 
				
			for (String str : pinyin1) {
				Set<String> pinyin2 = HanConvertPinYin(chars2[i]);
				boolean isContain=false;
				for (String str2 : pinyin2) {
					if (str.equals(str2)) {
						count++;
						isContain=true;
						break;
					}
				}
				if (isContain) {
					break;
				}
			}

		}  
		PinyinSim=2*count*1.0/(s1.length()+s2.length());
		return PinyinSim;
	}
	
	
	//获取读音相似度
	public double PinYinSimilar(List<String> newList, List<String> oldList){
		
		int count = 0;
		int MaxCount = 0;
		//新词长度
		int newLength = 0;
		//旧词长度
		int oldLength = 0;
		
		//读音完全相同
		int completeCount = 0;
		
		int total = 0;
		for (String newP : newList) {
			String news[] = newP.split(MyStaticValue.PINYIN_SPLIT_CHAR);			
			newLength = news.length;
			total += newLength;
			
			for (String oldP : oldList) {
				
				if(newP.equals(oldP)){
					completeCount++;
				}
				
				String olds[] = oldP.split(MyStaticValue.PINYIN_SPLIT_CHAR);
				oldLength = olds.length;
				total += oldLength;
				for (int i = 0; i < newLength && i < oldLength; i++) {
					if(news[i].equals(olds[i])){
						count++;
					}
				}
				
				if(MaxCount<count){
					MaxCount = count;
					count = 0;
				}				
			}			 
		} 
		PinyinSim=2*MaxCount*1.0/(total);
		
		if(completeCount>0){
			PinyinSim+=(1-PinyinSim)*0.8;
		}
		return PinyinSim;
		
	}
	//语义相似度
	public double SemanticSimilar(){
		SemanticSim = CoreSynonymDictionary.similarity(s1, s2);
		return SemanticSim;
	}
	
	@Override
	public String toString() { 
		return s1+"和"+s2+" 字形相似度："+PatternSim+"\t"+"字序相似度："+SequenceSim+"\t" +"读音相似度："+PinyinSim+"\t"
				+ "语义相似度："+SemanticSim;
	}	
	
	public boolean run(){
		double threadhold = MyStaticValue.SIMILAR_THREAHOLD;
		if (PatternSimilar()>=threadhold) {
			return true;
		}		
		if (SequenceSimilar()>=threadhold) {
			return true;
		}		
		if (PinYinSimilar()>=threadhold) {
			return true;
		}		
		if (SemanticSimilar()>=threadhold) {
			return true;
		}
		return false;
	}
	
	public boolean similarity(String newWord, String oldWord) {
		this.s1 = newWord;
		this.s2 = oldWord;
		return run();
	}
	
	public static void main(String[] args) {		
//		String s1="华大为";
//		String s2="大华为";
//		WordSimilarity similarity = new WordSimilarity(s1, s2);
//		similarity.run();
		WordSimilarity similarity = new WordSimilarity();
		similarity.PinyinSimilarity("天秤","天成");
		System.out.println(similarity.toString());
	}

	public double PatternSimilarity(String newWord, String oldWord) {
		// TODO Auto-generated method stub 
		this.s1 = newWord;
		this.s2 = oldWord;
		return PatternSimilar();
	}

	public double SequenceSimilarity(String newWord, String oldWord) {
		// TODO Auto-generated method stub
		this.s1 = newWord;
		this.s2 = oldWord;
		return SequenceSimilar();
	}

	public double PinyinSimilarity(String newWord,String oldWord) {
		// TODO Auto-generated method stub   
		
		List<String>  newList = getPinYinList(newWord);
		List<String>  oldList = getPinYinList(oldWord); 
		if(null == newList || 0 == newList.size()){
			return 0.0;
		}
		
		if(null == oldList || 0 == oldList.size()){
			return 0.0;
		}		
		return PinYinSimilar(newList,oldList);
	}

	public List<String> getPinYinList(String word) {
		List<String> list = new ArrayList<String>();		
		List<HashSet<String>> setLists = new ArrayList<HashSet<String>>();
		char[] chars = word.toCharArray();
		for(int i=0;i<chars.length;i++){ 
			HashSet<String> set= new HashSet<String>();
			set = HanConvertPinYin(chars[i]);
			setLists.add(set);
		} 
		String pre = new String();
		traverse(0,pre,setLists,list);
		return list;
	}
	
	
	public int traverse(int iDeep,String pre, List<HashSet<String>> setLists,List<String> list){
//		System.out.println("ideep :" +iDeep);   
//		System.out.println("pre :" +pre);
    	
        if (iDeep == setLists.size()-1){        	
        	for(String  c : setLists.get(iDeep)){
        		String tmp = new String();
        		tmp = pre + c;
        		list.add(tmp);
        	}
        	iDeep=0;
        	pre="";
            return 1;
        }
        HashSet<String> tmpSet = setLists.get(iDeep);
        String tmpPre = new String();
        for(String cc : tmpSet){
        	tmpPre = pre + cc+",";
        	iDeep++;
        	if(iDeep>=setLists.size()){
        		return 0;
        	}
        	//每层循环结束 ideep --
        	if(traverse(iDeep,tmpPre,setLists,list)==1){
        		iDeep--;
        	}
        	tmpPre = pre;
        	continue;
        }

        return 1;
    } 
	
	
	public double SemanticSimilarity(String newWord, String oldWord) {
		this.s1 = newWord;
		this.s2 = oldWord;
		return SemanticSimilar();
	}

	public double PinyinSimilarity(List<String> newList, List<String> oldList) {
		return PinYinSimilar(newList,oldList);
	} 
}
