// Puzzle8Dlg.h : header file
//

#if !defined(AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_)
#define AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_
#include "Node.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include "SHDIalog.h"
#include <queue>
using namespace std;
/////////////////////////////////////////////////////////////////////////////
// CPuzzle8Dlg dialog

class CPuzzle8Dlg : public CDialog
{
// Construction
public:
	CString m_strPrompt;
	queue <Node> best;
	Node* m_pPuzzle;		
	CString m_strFactor;
	CPuzzle8Dlg(CWnd* pParent = NULL);	// standard constructor
    CSHDIalog hdlg;
// Dialog Data
	//{{AFX_DATA(CPuzzle8Dlg)
	enum { IDD = IDD_PUZZLE8_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPuzzle8Dlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;
	// Generated message map functions
	//{{AFX_MSG(CPuzzle8Dlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnOptionsExit();
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	afx_msg void OnOptionsSelectGreedyMethod();
	afx_msg void OnOptionsNewgame();
	afx_msg void OnOptionsSolve();
	afx_msg void OnHelpAbout();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PUZZLE8DLG_H__81B56090_93D4_4406_9E6A_0787C12E0D4C__INCLUDED_)
