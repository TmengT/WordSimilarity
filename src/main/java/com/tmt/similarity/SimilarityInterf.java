package com.tmt.similarity;

import java.util.List;

/**
 * 
 * 文件名：SimilarityInterf.java
 * 类描述：词相似度判断接口
 * 作者：ywc
 * 创建时间：2015年5月7日
 * 修改人：
 * 修改备注：
 */
public interface SimilarityInterf {
	/**
	 * 
	 * 函数名：similarity
	 * 功能描述：
	 * 入参：String newWord  新词
	 * 	    String oldWord  旧词
	 * 出参：boolean true 是相似词， false 不是相似词
	 * 作者：ywc
	 * 创建时间：2015年5月7日
	 * 修改人：
	 * 修改备注：
	 */
	boolean similarity(String newWord,String oldWord);

	/**
	 * 
	 * 函数名：PatternSimilarity
	 * 功能描述：字形相似度
	 * 入参： String newWord  新词
	 * 	    String oldWord  旧词
	 * 出参：double 相似度系数
	 * 作者：ywc
	 * 创建时间：2015年5月21日
	 * 修改人：
	 * 修改备注：
	 */
	double PatternSimilarity(String newWord,String oldWord);
	
	
	/**
	 * 
	 * 函数名：SequenceSimilarity
	 * 功能描述：字序相似度
	 * 入参：String newWord  新词
	 * 	    String oldWord  旧词
	 * 出参：double 相似度系数
	 * 作者：ywc
	 * 创建时间：2015年5月21日
	 * 修改人：
	 * 修改备注：
	 */
	double SequenceSimilarity(String newWord,String oldWord);
	/**
	 * 
	 * 函数名：PinyinSimilarity
	 * 功能描述：读音相似度
	 * 入参：String newWord  新词
	 * 	    String oldWord  旧词
	 * 出参：double 相似度系数
	 * 作者：ywc
	 * 创建时间：2015年12月15日
	 * 修改人：
	 * 修改备注：
	 */
	double PinyinSimilarity(String newWord,String oldWord);	
	
	/**
	 * 
	 * 函数名：SemanticSimilarity
	 * 功能描述：语义相似度
	 * 入参： String newWord  新词
	 * 	    String oldWord  旧词
	 * 出参：double 相似度系数
	 * 作者：ywc
	 * 创建时间：2015年5月21日
	 * 修改人：
	 * 修改备注：
	 */
	double SemanticSimilarity(String newWord,String oldWord);
	
	
	
	/**
	 * 
	 * 函数名：getPinYinList
	 * 功能描述：词读音列表 
	 * 入参：String word
	 * 出参：词读音列表
	 * 作者：ywc
	 * 创建时间：2015年12月15日
	 * 修改人：
	 * 修改备注：
	 */
	
	List<String> getPinYinList(String word);
	
	/**
	 * 
	 * 函数名：PinyinSimilarity
	 * 功能描述：读音相似度
	 * 入参：List<String> newList 新词读音List
	 * 	    List<String> oldList 旧词读音List
	 * 出参：double 相似度系数
	 * 作者：ywc
	 * 创建时间：2015年12月15日
	 * 修改人：
	 * 修改备注：
	 */
	double PinyinSimilarity(List<String> newList,List<String> oldList);	
}
