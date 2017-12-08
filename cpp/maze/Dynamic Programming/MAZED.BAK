#include<iostream.h>
#include<conio.h>
#include<stdio.h>
///////////////////////////////////////////////////
/*
  Ali Javadzadeh 7913801
  Maze:Dynamic Programming
*/
///////////////////////////////////////////////////
int Maze(int,int);
///////////////////////////////////////////////////
void main(void)
{

  int n,m;
  clrscr();
  cout<<"Enter Number Of Rows   : ";
  cin>>m;
  cout<<"Enter Number Of Columns: ";
  cin>>n;
  cout<<endl;
  cout<<"\nNumber of answers:"<<Maze(m,n);
  getch();
}
int Maze(int m,int n)
{
     int**maze=new int*[m+1];
     printf("       ");
     for(int x=1;x<=m;x++)
     {
	 maze[x][1]=1;
	 printf("%4d.",x);
     }
     cout<<endl<<endl;
     for(int y=1;y<=n;y++)
	 maze[1][y]=1;
     for(x=2;x<=m;x++)
       for(int y=2;y<=n;y++)
	  maze[x][y]=maze[x][y-1]+maze[x-1][y];
     for(x=1;x<=m;x++)
     {
       printf("%5d.",x);
       for(y=1;y<=n;y++)
	  printf("%5d",maze[x][y]);
       cout<<endl;
     }
     return maze[m][n];
}
