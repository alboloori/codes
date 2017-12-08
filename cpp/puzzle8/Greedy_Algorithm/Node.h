////////////////////////////////////////////////////////////
#include <math.h>
////////////////////////////////////////////////////////////
enum Side{L,R,U,D,I};//Left,Right,Up,Down,Initialize
////////////////////////////////////////////////////////////
class Node
{
  public:
	Node(char* p=0)
	{
      if(p)
	  {
        int k=0;
        for(int i=0;i<3;i++)
          for(int j=0;j<3;j++)
		  {
            state[i][j]=p[k++];
            if(p[k-1]==' ')
			{
             blank.x=j;
             blank.y=i;
			}
		  }
	  }
        level=0;
	    side=I;
	}
	Node& operator=(const Node& p)
	{
		for(int i=0;i<3;i++)
         for(int j=0;j<3;j++)
		 {
           state[i][j]=p.state[i][j];
           blank=p.blank;
		   level=p.level;
		   side=p.side;
		 }
		return *this;
	}
	////////////////////////////////////////////////////////////
	bool CanMoveTo(Side aim,Side parent_side)
	{
		bool flag=true;
		switch(aim)
		{
		  case L:
            if(blank.x==0 || parent_side==R)
			   flag=false;
		  break;
          case R:
            if(blank.x==2 ||parent_side==L)
			   flag=false; 
          break;
		  case U:
            if(blank.y==0 || parent_side==D)
			   flag=false;
		  break;
		  case D:
            if(blank.y==2 || parent_side==U)
			   flag=false;
		  break;
		}
		return flag;
	}
	////////////////////////////////////////////////////////////
	int  Greedy(int h,const Node& goal)
	{
		if(h==1) return Greedy1(goal);
		return Greedy2(goal);
	}
	////////////////////////////////////////////////////////////
	int  Greedy1(const Node& goal)
	{
      int count=0,x=0,y=0,i;
     for( i=0;i<3;i++)
	 {   for(int j=0;j<3;j++)
		    if(state[i][j]!=' ')
				if(goal.state[i][j]!=state[i][j])
				count++;	  
	 }
      return count;
	}
   ////////////////////////////////////////////////////////////
	int  Greedy2(const Node& goal)
	{		
      //Manhatan Distance
      int count=0,x=0,y=0;
         for(int i=0;i<3;i++)
	        for(int j=0;j<3;j++)
		    if(state[i][j]!=' ')
			{
             int k,l;
		     for(k=0;k<3;k++)
			   for(l=0;l<3;l++)
				  if(goal.state[k][l]==state[i][j])
				  {
					  x=k;
					  y=l;
					  l=k=3;
				  }
		     count+=abs(x-i)+abs(y-j);
			}
      return count;
	}
	////////////////////////////////////////////////////////////
	bool IsGoal(const Node& goal,int h)
	{
	  if(h==1 && Greedy1(goal)==0 )return true;
	  if(h==2 && Greedy2(goal)==0 )return true;	  
      return false;
	}
    ////////////////////////////////////////////////////////////
	void Move(Side aim )
	{      
       side=aim;
       level++;
       switch (aim)
		{
          case L:
			{
              state[blank.y][blank.x]=state[blank.y][blank.x-1];
              state[blank.y][blank.x-1]=' ';
              blank.x--;
			}
		  break;  
          case R:
			{
              state[blank.y][blank.x]=state[blank.y][blank.x+1];
              state[blank.y][blank.x+1]=' ';
              blank.x++;
			}
		  break;
          case U:
			{
              state[blank.y][blank.x]=state[blank.y-1][blank.x];
              state[blank.y-1][blank.x]=' ';
              blank.y-=1;
			}
	      break;
          case D:
			{
              state[blank.y][blank.x]=state[blank.y+1][blank.x];
              state[blank.y+1][blank.x]=' ';
              blank.y+=1;
			}
		  break;
		}
	}
	////////////////////////////////////////////////////////////
	int  level;
	char state[3][3];
	Side side;
	CPoint blank;
};
////////////////////////////////////////////////////////////
