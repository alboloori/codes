// Puzzle8Dlg.cpp : implementation file
//

#include "stdafx.h"
#include "Puzzle8.h"
#include "Puzzle8Dlg.h"
#include <queue>
#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif
const UNIT=50;
const XT=80,YT=80,XB=80+3*UNIT,YB=80+3*UNIT;
using namespace std ;

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPuzzle8Dlg dialog

CPuzzle8Dlg::CPuzzle8Dlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPuzzle8Dlg::IDD, pParent)
{
		//{{AFX_DATA_INIT(CPuzzle8Dlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_pPuzzle=new Node("1238 4765");
}

void CPuzzle8Dlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CPuzzle8Dlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CPuzzle8Dlg, CDialog)
	//{{AFX_MSG_MAP(CPuzzle8Dlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_COMMAND(ID_OPTIONS_EXIT, OnOptionsExit)
	ON_WM_KEYDOWN()
	ON_COMMAND(ID_OPTIONS_SELECT_GREEDYMETHOD, OnOptionsSelectGreedyMethod)
	ON_COMMAND(ID_OPTIONS_NEWGAME, OnOptionsNewgame)
	ON_COMMAND(ID_OPTIONS_SOLVE, OnOptionsSolve)
	ON_COMMAND(ID_HELP_ABOUT, OnHelpAbout)
	ON_WM_LBUTTONDOWN()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPuzzle8Dlg message handlers

BOOL CPuzzle8Dlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.
    
	
	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here
	
	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CPuzzle8Dlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CPuzzle8Dlg::OnPaint() 
{
	CPaintDC dc(this);
	if (IsIconic())
	{	
		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);
		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;
		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
	  CBrush brush;
	  CBrush blankbrush;
	  blankbrush.CreateSolidBrush(RGB(0,0,255));
	  CPen pen;
	  CRect rect;
	  CRect blankrect;
	  CFont font;
	  brush.CreateSolidBrush(RGB(0,255,255));
	  pen.CreatePen(PS_SOLID,4,RGB(0,85,170));
	  rect.SetRect(XT,YT,XB,YB);
	  font.CreateFont(30,30,0,0,0,0,0,0,0,0,0,0,0,"Arial Black");
	  dc.SelectObject(&brush);
	  dc.SelectObject(&pen);
	  dc.FillRect(rect,&brush);
	  dc.SelectObject(&font);
	  dc.SetBkMode(TRANSPARENT);
	  dc.MoveTo(XT,YT);
	  dc.LineTo(XB,YT);
	  dc.MoveTo(XT,YT);
	  dc.LineTo(XT,YB);
	  dc.MoveTo(XT,YB);
	  dc.LineTo(XB,YB);
	  dc.MoveTo(XB,YT);
	  dc.LineTo(XB,YB);
	  dc.MoveTo(XT,YT+UNIT);
	  dc.LineTo(XB,YT+UNIT);
	  dc.MoveTo(XT,YT+2*UNIT);
	  dc.LineTo(XB,YT+2*UNIT);
	  dc.MoveTo(XT+UNIT,YT);
	  dc.LineTo(XT+UNIT,YB);
	  dc.MoveTo(XT+2*UNIT,YT);
	  dc.LineTo(XT+2*UNIT,YB);
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
			{
				dc.TextOut(XT+j*UNIT+10,YT+i*UNIT+10,m_pPuzzle->state[i][j]);
				if(m_pPuzzle->state[i][j]==' ') 
				{
					dc.SelectObject(&blankbrush);
					blankrect.SetRect(XT+j*UNIT,YT+i*UNIT,XT+(j+1)*UNIT,YT+(i+1)*UNIT);
                    dc.FillRect(blankrect,&blankbrush);
				}
			}
		CFont font2;
		font2.CreateFont(20,15,0,0,FW_BOLD,1,0,0,0,0,0,0,FF_SCRIPT,"Monotype Corsiva");
		dc.SelectObject(&font2);
		dc.TextOut(200,30,"The 8-Puzzle");
		CFont font3;
		font3.CreateFont(15,10,0,0,FW_BOLD,1,0,0,0,0,0,0,0,"Arial");
		dc.SelectObject(&font3);
		dc.TextOut(260,88,m_strPrompt);
		dc.TextOut(260,120,m_strFactor);
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CPuzzle8Dlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}



void CPuzzle8Dlg::OnOptionsExit() 
{
	// TODO: Add your command handler code here
	if(AfxMessageBox("Quit\nAre you sure?",MB_ICONQUESTION|MB_YESNO)==IDYES)OnOK();
}

void CPuzzle8Dlg::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	switch(nChar)
	{
	case VK_NUMPAD4://Left
		{  if(m_pPuzzle->CanMoveTo(L,I)==true)
			  m_pPuzzle->Move(L);
			break;
		}
	case VK_NUMPAD6://Right
		{
			if(m_pPuzzle->CanMoveTo(R,I)==true)
			   m_pPuzzle->Move(R);
			break;
		}
	case VK_NUMPAD8://Up
		{
			if(m_pPuzzle->CanMoveTo(U,I)==true)
			   m_pPuzzle->Move(U);
			break;
		}
	case VK_NUMPAD2://Down
		{			
            if(m_pPuzzle->CanMoveTo(D,I)==true)
			   m_pPuzzle->Move(D);
			break;
		}
	}
    Invalidate();
	CDialog::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CPuzzle8Dlg::OnOptionsSelectGreedyMethod() 
{
	// TODO: Add your command handler code here
	hdlg.DoModal();
}

void CPuzzle8Dlg::OnOptionsNewgame() 
{
	// TODO: Add your command handler code here
	hdlg.m_iGreedy=1;
	m_strFactor="";
	m_strPrompt="";
	delete(m_pPuzzle);
	m_pPuzzle=new Node("1238 4765");
	Invalidate();
}

void CPuzzle8Dlg::OnOptionsSolve() 
{
	// TODO: Add your command handler code here
	int   h=hdlg.m_iGreedy,same_h=0;
	m_pPuzzle->side=I;
    m_pPuzzle->level=0;
	//Node parent=*m_pPuzzle;
	Node node=*m_pPuzzle; 
	Side sides[4]={L,R,U,D};
	Node goal("1238 4765");
	queue <Node> compare;	 
	while(node.IsGoal(goal,h)==false)
	{    
		Node parent;
		parent=node;
		if(node.CanMoveTo(L,parent.side)==true)
		{   			
			node.Move(L);
	    	compare.push(node);
		}
	  else 
	  {
	   if(node.CanMoveTo(R,parent.side)==true)
	   {
		node.Move(R);
	    compare.push(node);	
	   }
	   else
	   	   if(node.CanMoveTo(U,parent.side)==true)
		   {
			  node.Move(U);
		      compare.push(node);
		   }
	       else {
                   node.Move(D);
				   compare.push(node);					   
		   }
	  }
	
	for(int i=0;i<4;i++)
      if(node.side!=sides[i])
	  {
        if(parent.CanMoveTo(sides[i],parent.side))
		{
          Node sibling=parent;
          sibling.Move(sides[i]);
		  if(sibling.Greedy(h,goal)<compare.front().Greedy(h,goal))
		  {
            compare.pop();
			compare.push(sibling);
		  }
		  /*else if(sibling.Greedy(h,goal)==compare.front().Greedy(h,goal))
           compare.push(sibling);
		  same_h++;
		}*/

		}
	  }
		node=compare.front();
		compare.pop();
		best.push(node);
	
	}//while*/
	AfxMessageBox("Goal State Found.\nPlease Click Left To Watch The Moves");
	m_strPrompt="The Path To The Goal Is:";
	Invalidate();
}

void CPuzzle8Dlg::OnHelpAbout() 
{
	// TODO: Add your command handler code here
	CAboutDlg hedlg;
	hedlg.DoModal();
}

void CPuzzle8Dlg::OnLButtonDown(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	int  count='0';
	
	if(best.empty()==false)
	{
     *m_pPuzzle=best.front();
	 count++;
	 switch(m_pPuzzle->side)
	 {
	 case L:
       m_strFactor+="L ";
     break;
	 case R:
       m_strFactor+="R ";
	 break;
	 case U:
        m_strFactor+="U ";
	   break;
	 case D:
       m_strFactor+="D ";
	   break;
	 }
	 best.pop();
	 Invalidate();
	}
	CDialog::OnLButtonDown(nFlags, point);
}
