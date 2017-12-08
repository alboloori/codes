#include<iostream.h>
#include<conio.h>
///////////////////////////////////////////////////
/*
  Ali Javadzadeh 7913801
  Maze:Divide & Conquer
*/
///////////////////////////////////////////////////
const MAXN=10;
char m[MAXN][MAXN];
///////////////////////////////////////////////////
void Draw_Maze(int ex,int ey)
{
    static int result_n=0;

    clrscr();
    gotoxy(30-ey/2,10+ey);
    cout<<"Result :"<<++result_n;

    for(int i=0;i<=ex;i++)
    {
	gotoxy(35-ey/2,5+i);
	for(int j=0;j<=ey;j++)
	     cout<<char('0'+m[i][ey-j]);
    }
    getch();

}
///////////////////////////////////////////////////
void Maze(int sx,int sy,int ex,int ey)
{
  if( ((sx==ex) && (sy==ey)) || (m[ex][ey]) )
  {
     Draw_Maze(ex,ey);
     m[ex][ey]=0;
  }

  else
  {
     if(sx<ex)//up
     {
       m[sx+1][sy]=1;
       Maze(sx+1,sy,ex,ey);
       m[sx+1][sy]=0;
     }

     if(sy<ey)  //right
     {
       m[sx][sy+1]=1;
       Maze(sx,sy+1,ex,ey);
       m[sx][sy+1]=0;
     }

  }
}
///////////////////////////////////////////////////
void main(void)
{
  int n,l;
  clrscr();
  cout<<"Enter n: ";cin>>n;cout<<'\n';
  cout<<"Enter m: ";cin>>l;cout<<'\n';

  for(int i=0;i<MAXN;i++)
      for(int j=0;j<MAXN;j++)
	m[i][j]=0;
  m[0][0]=1;

  Maze(0,0,n-1,l-1);
  getch();

}