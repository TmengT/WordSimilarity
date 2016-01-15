package com.inspur.similarity;

import com.hankcs.hanlp.HanLP;



/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	 String str = HanLP.segment("住了这么多旅店，还是七天比较放心，价格也很合适107大床房，挺干净的，之前订了一家旅店，价格差不多，可是连卫生间都没有，果断退掉了，位置也还不错，就在大明湖西南门旁边不远，吃饭逛大明湖什么的都很近，满意").toString();
    	
    	System.out.print(str);
    	/*SimilarityInterf inter = new WordSimilarity();
    	if (inter.similarity("火炎焱燚", "院火")) {
    		System.out.println("两词相似！");
		}else {
			System.out.println("两词不相似！");
		}

    	/*List<String> newList = new ArrayList<String>();
    	List<String> oldList = new ArrayList<String>();
    	newList.add("xiang,si,du,jian,ce,zhong,xin");
    	oldList.add("shen,zhen,shi,xin,xin,shi,jian,zhu,zhuang,shi,cai,liao,you,xian,gong,si");
    	oldList.add("xiang,si,du,jian");
    	oldList.add("xiang,si,du,jian");
    	double b = inter.PinyinSimilarity(newList, oldList);*/
    	
    	//打印 相似维度数据
        //System.out.println(b); 
    	
    	//打印 相似维度数据
        //System.out.println(inter.toString());*/
    }
}
