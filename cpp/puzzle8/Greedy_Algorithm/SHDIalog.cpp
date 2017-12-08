// SHDIalog.cpp : implementation file
//

#include "stdafx.h"
#include "Puzzle8.h"
#include "SHDIalog.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CSHDIalog dialog


CSHDIalog::CSHDIalog(CWnd* pParent /*=NULL*/)
	: CDialog(CSHDIalog::IDD, pParent)
{
	//{{AFX_DATA_INIT(CSHDIalog)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
 m_iHeuristic=1;
}


void CSHDIalog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CSHDIalog)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CSHDIalog, CDialog)
	//{{AFX_MSG_MAP(CSHDIalog)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CSHDIalog message handlers

void CSHDIalog::OnOK() 
{
	// TODO: Add extra validation here
 switch(((CComboBox*)GetDlgItem(IDC_COMBO_HEURISTIC))->GetCurSel())
 {
	
 case 0:
	 m_iHeuristic=1;
	 break;
 case 1:
	 m_iHeuristic=2;
	 break;
 }
	CDialog::OnOK();
}
