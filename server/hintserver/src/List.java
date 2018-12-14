import java.math.*;
import java.util.Random;


public class List {
	//8*8的1矩阵
	int Array[][]=new int[8][8];
	int length=8;
	//生成8*8矩阵并防止有3个连续的连在一起
	public List()
	{
	
		Random random=new Random();
		int color;
		for(int i=0;i<length;i++)
		{
			for(int j=0;j<length;j++)
			{
				color=random.nextInt(7);
				Array[i][j]=color;
				if(i<2&&j>=2)
				{
					while(autocheckarray(i,j)>0)
						Array[i][j]=random.nextInt(7);	
				}
				else if(i>=2&&j<2)
				{
					while(autocheckcloumn(i,j)>0)
						Array[i][j]=random.nextInt(7);	
				}
				else if(i>=2&&j>=2)
				{
					while(autocheckcloumn(i,j)>0||autocheckarray(i,j)>0)
						Array[i][j]=random.nextInt(7);	
				}												
			}
		}
		
	}
	// 从字符串初始化list
	public List(String maxtrix)
	{
		char[] elements=maxtrix.toCharArray();
		int k=0;
		int num=elements.length/2;
		int arrays=0;
		if(num%length!=0)
			arrays=num/length+1;
		else
			arrays=num/length;
		for(int i=0;i<arrays;i++)
		{
			if(k>=(elements.length-1))
				break;
			for(int j=0;j<length;j++)
			{
				if(elements[k]!=' '&&k<(elements.length-1))
				{ 
					Array[i][j]=Integer.parseInt(String.valueOf(maxtrix.charAt(k)));
					k++;
				}
				else if(elements[k]==' '&&k<(elements.length-1))
				{
					k++;
					Array[i][j]=Integer.parseInt(String.valueOf(maxtrix.charAt(k)));
					k++;
				}
				else if(k>=(elements.length-1))
				{
					break;
				}
			}
		}
		
	}
	//从第三个向前检查1和2和3是否同行，比如A、B、C坐标为1、2、3，则从C开始，检查A、B是否为同色
	public int autocheckarray(int i,int j)
	{
		if(j<2)
			return -1;
		else if(Array[i][j]==Array[i][j-2]&&Array[i][j]==Array[i][j-1])
			return j;
		else 
			return -2;
	}
	//从第三个向前检查1和2和3是否同列，比如A、B、C坐标为1、2、3，则从C开始，检查A、B是否为同色
	public int autocheckcloumn(int i,int j)
	{
		if(i<2)
			return -1;
		else if(Array[i][j]==Array[i-1][j]&&Array[i][j]==Array[i-2][j])
			return i;
		else 
			return -2;
	}
	//测试函数
	public int[] test()
	{ 
		List list=this;
		Pairs pairs=new Pairs();//对儿为自己定义的返回x、y坐标的自定义类
		int x,y=0;

		//自动玩一次并判断是否有可以交换消除的位置
		pairs=list.autoplay(list);
		int[] ret = {-1, -1};

		if(pairs.getfirst()>=0||pairs.getSecond()>=0)
		{
			ret[0] = pairs.getfirst()+1;
			ret[1] = pairs.getSecond()+1;

			return ret;
		}
		else
		{
			return ret;
		}	
	}
	//为自动玩的函数autoplay提供一个封装的方法，该方法会尝试交换两个宝石，然后用之前封装的列检查函数来判断是否3个同色，这样虽然比直接判断要多两步交换的步骤，
	//却减少了写代码时思考坐标的任务量，是一个偷懒操作。
	public Pairs autochangeforcolumn(int i1,int i2,int i3,int j1,int j2,int j3,List list)
	{
		Pairs pairs=new Pairs();
		pairs.add(-1, -1);
		int temp;
		int test=-1;
		temp=list.Array[i1][j1];
		list.Array[i1][j1]=list.Array[i2][j2];
		list.Array[i2][j2]=temp;
		test=list.autocheckcloumn(i3,j3);
		temp=list.Array[i1][j1];
		list.Array[i1][j1]=list.Array[i2][j2];
		list.Array[i2][j2]=temp;
		if(test>=0)
		{

			pairs.add(i1, j1);
		}

		return pairs;
		
	}
	//为自动玩的函数autoplay提供一个封装的方法，该方法会尝试交换两个宝石，然后用之前封装的行检查函数来判断是否3个同色，这样虽然比直接判断要多两步交换的步骤，
	//却减少了写代码时思考坐标的任务量，是一个偷懒操作。
	public Pairs autochangeforarray(int i1,int i2,int i3,int j1,int j2,int j3,List list)
	{
		Pairs pairs=new Pairs();
		pairs.add(-1, -1);
		int temp;
		int test=-1;
		temp=list.Array[i1][j1];
		list.Array[i1][j1]=list.Array[i2][j2];
		list.Array[i2][j2]=temp;
		test=list.autocheckarray(i3,j3);
		temp=list.Array[i1][j1];
		list.Array[i1][j1]=list.Array[i2][j2];
		list.Array[i2][j2]=temp;
		if(test>=0)
		{
			pairs.add(i1, j1);
		}

		return pairs;
		
	}
	//主要函数：autoplay，模拟自动玩一次设计的函数，一共14种情况考虑，并对边界做了判断和减少比较操作，可以通过传递List的方法来使用矩阵，
	//如果调用对象的Array可以被直接使用，而不需要当做参数传递的话。如果是打算直接用Array的方法，需要将下面所有list.Array改成Array。
	public Pairs autoplay(List list)
	{
		Pairs pairs=new Pairs();

		try {
			for(int i=0;i<list.length;i++)
			{
				for(int j=0;j<list.length;j++)
				{
					if(i>5||j>5)//开始除左上区域的检测
					{
							if(i<=5)
							{
								if(list.Array[i][j]==list.Array[i+1][j])
								{
									pairs=autochangeforcolumn(i+2,i+2,i+2,j,j-1,j,list);
									if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
										return pairs;
								
									if(i!=5)
									{
										pairs=autochangeforcolumn(i+2,i+3,i+2,j,j,j,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
		
									
											
									if(j!=7)
									{	
										pairs=autochangeforcolumn(i+2,i+2,i+2,j,j+1,j,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
										
								}
								if(list.Array[i][j]==list.Array[i+2][j])
								{
									pairs=autochangeforcolumn(i+1,i+1,i+2,j,j-1,j,list);
									if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
										return pairs;
									if(j!=7)
									{
										pairs=autochangeforcolumn(i+1,i+1,i+2,j,j+1,j,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
									if(i<=4)
									{
										pairs=autochangeforcolumn(i,i+1,i+3,j,j,j,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
								}
								if(list.Array[i][j]==list.Array[i+1][j+1])
								{
									if(j!=7)
									{
										pairs=autochangeforcolumn(i,i,i+2,j,j+1,j+1,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
								}
							}//右上部分校验完成
							else if(j<=5)
							{
								if(list.Array[i][j]==list.Array[i][j+1])
								{
									pairs=autochangeforarray(i,i-1,i,j+2,j+2,j+2,list);
									if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
										return pairs;
									if(j!=5)
									{
										pairs=autochangeforarray(i,i,i,j+2,j+3,j+2,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
									if(i!=7)
									{
										pairs=autochangeforarray(i,i+1,i,j+2,j+2,j+2,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
								}
								if(list.Array[i][j]==list.Array[i][j+2])
								{
									pairs=autochangeforarray(i,i-1,i,j+1,j+1,j+2,list);
									if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
										return pairs;
									if(i!=7)
									{
										pairs=autochangeforarray(i,i+1,i,j+1,j+1,j+2,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
									if(j<=4)
									{
										pairs=autochangeforarray(i,i,i,j,j+1,j+3,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
								}
								if(list.Array[i][j]==list.Array[i+1][j+1])
								{
									if(i!=7)
									{
										pairs=autochangeforarray(i,i+1,i+1,j,j,j+2,list);
										if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
											return pairs;
									}
								}
							}//左下部分校验完成
							
					}
					else//开始左上区域的情况监测
					{
						if(list.Array[i][j]==list.Array[i+1][j])
						{
							if(j!=0)
							{
								pairs=autochangeforcolumn(i+2,i+2,i+2,j,j-1,j,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}
							
							if(i<5)
							{
								pairs=autochangeforcolumn(i+2,i+3,i+2,j,j,j,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}
								
							pairs=autochangeforcolumn(i+2,i+2,i+2,j,j+1,j,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
						}
						else if(list.Array[i][j]==list.Array[i][j+1])
						{
							if(i!=0)
							{
								pairs=autochangeforarray(i,i-1,i,j+2,j+2,j+2,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}

							if(j<5)
							{
								pairs=autochangeforarray(i,i,i,j+2,j+3,j+2,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}

							
									
								
							pairs=autochangeforarray(i,i+1,i,j+2,j+2,j+2,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
						}//完成了第一种情况的校验
						else if(list.Array[i][j]==list.Array[i+2][j])//开始第二种和第三种校验
						{
							if(j!=0)
							{
								pairs=autochangeforcolumn(i+1,i+1,i+2,j,j-1,j,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}
							pairs=autochangeforcolumn(i+1,i+1,i+2,j,j+1,j,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
							//第三种情况1
							pairs=autochangeforcolumn(i,i+1,i+3,j,j,j,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
							
						}
						else if(list.Array[i][j]==list.Array[i][j+2])
						{
							if(i!=0)
							{
								pairs=autochangeforarray(i,i-1,i,j+1,j+1,j+2,list);
								if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
									return pairs;
							}
							pairs=autochangeforarray(i,i+1,i,j+1,j+1,j+2,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
							//第三种情况2
							pairs=autochangeforarray(i,i,i,j,j+1,j+3,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
							
						}//完成第二种和第三种的部分校验
						else if(list.Array[i][j]==list.Array[i+1][j+1])
						{
							pairs=autochangeforarray(i,i+1,i+1,j,j,j+2,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
							pairs=autochangeforcolumn(i,i,i+2,j,j+1,j+1,list);
							if(pairs.getfirst()>=0&&pairs.getSecond()>=0)
								return pairs;
						}//完成第三种校验,左上全部校验完成
					}
				}
			}
			pairs.add(-1, -1);//这里设计比较笨，-1这个数字小于0，则看成是没找到可用位置。可以适当根据宝石数字变化来更改pairs.getfirst和pairs.getsecond的边界判断条件。
			return pairs;
		}
		catch (Exception e) {
			pairs.add(-1, -1);//这里设计比较笨，-1这个数字小于0，则看成是没找到可用位置。可以适当根据宝石数字变化来更改pairs.getfirst和pairs.getsecond的边界判断条件。
			return pairs; 
		}
	}
}
