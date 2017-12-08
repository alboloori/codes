// DepthDialog.cpp : implementation file
//

#include "stdafx.h"
#include "Puzzle8.h"
#include "DepthDialog.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDepthDialog dialog


CDepthDialog::CDepthDialog(CWnd* pParent /*=NULL*/)
	: CDialog(CDepthDialog::IDD, pParent)
{
	//{{AFX_DATA_INIT(CDepthDialog)
	m_iDepth = 15;
	//}}AFX_DATA_INIT
}


void CDepthDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CDepthDialog)
	DDX_Text(pDX, IDC_EDIT_DEPTH, m_iDepth);
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CDepthDialog, CDialog)
	//{{AFX_MSG_MAP(CDepthDialog)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDepthDialog message handlers

void CDepthDialog::OnOK() 
{
	// TODO: Add extra validation here
	UpdateData(TRUE);
	CDialog::OnOK();
}
