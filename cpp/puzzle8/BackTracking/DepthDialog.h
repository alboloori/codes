#if !defined(AFX_DEPTHDIALOG_H__95E26D8A_FEC0_4059_AB4A_6001B89F6321__INCLUDED_)
#define AFX_DEPTHDIALOG_H__95E26D8A_FEC0_4059_AB4A_6001B89F6321__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// DepthDialog.h : header file
//

/////////////////////////////////////////////////////////////////////////////
// CDepthDialog dialog

class CDepthDialog : public CDialog
{
// Construction
public:
	CDepthDialog(CWnd* pParent = NULL);   // standard constructor

// Dialog Data
	//{{AFX_DATA(CDepthDialog)
	enum { IDD = IDD_DIALOG_DEPTH };
	int	m_iDepth;
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDepthDialog)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(CDepthDialog)
	virtual void OnOK();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DEPTHDIALOG_H__95E26D8A_FEC0_4059_AB4A_6001B89F6321__INCLUDED_)
