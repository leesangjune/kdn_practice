package com.kdn.model.biz;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kdn.model.domain.Board;
import com.kdn.model.domain.FileBean;
import com.kdn.model.domain.PageBean;
import com.kdn.model.domain.UpdateException;
import com.kdn.util.DBUtil;
import com.kdn.util.PageUtility;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	@Qualifier("boardDao")
	private BoardDao dao;
	@Override
	public void add(Board board, String dir) {
		Connection con = null;
		File[ ] files = null;
		int size = 0;
		try {
			con = DBUtil.getConnection();
			
			int bno = dao.getBoardNo(con);
			board.setNo(bno);
			dao.add(con, board);
			
			MultipartFile[] fileup = board.getFileup();
			if(fileup!=null){
				size = fileup.length;
				files = new File[size];
				ArrayList<FileBean> fileInfos = new ArrayList<FileBean>(size);
				String rfilename = null;
				String sfilename = null;
				int index = 0;
				for (MultipartFile file : fileup) {
					rfilename = file.getOriginalFilename();
					sfilename = String.format("%d%s", System.currentTimeMillis(), rfilename);
					
					fileInfos.add(new FileBean(rfilename, sfilename));
					String fileName = String.format("%s/%s", dir,sfilename);
					files[index] = new File(fileName);
					file.transferTo(files[index++]);
				}
				dao.addFiles(con, fileInfos, bno);
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			DBUtil.rollback(con);
			if(files!=null){ 
				//오류가 발생해서 롤백하기 때문에 저장한 파일이 있다면 삭제
				for (File file : files) {
					//해당 파일이 지정한 경로에 존재하면
					if(file!=null && file.exists()){
						file.delete(); //파일 삭제
					}
				}
				
			}
			throw new UpdateException("게시글 작성 중 오류 발생");
		} finally {
			DBUtil.close(con);
		}
	}

	@Override
	public void update(Board board) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			dao.update(con, board);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UpdateException("게시글 수정 중 오류 발생");
		} finally {
			DBUtil.close(con);
		}
	}

	@Override
	public void remove(int no) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			dao.removeFiles(con, no);
			dao.remove(con, no);
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtil.rollback(con);
			throw new UpdateException("게시글 삭제 중 오류 발생");
		} finally {
			DBUtil.close(con);
		}
	}
	@Override
	public Board search(int no) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			return dao.search(con, no);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UpdateException("게시글 검색 중 오류 발생");
		} finally {
			DBUtil.close(con);
		}
	}
	@Override
	public List<Board> searchAll(PageBean bean) {
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			int total = dao.getCount(con, bean);
			
			PageUtility bar = 
			  new PageUtility(bean.getInterval()
					  		, total
					  		, bean.getPageNo()
					  		, "images/");
			bean.setPagelink(bar.getPageBar());
			
			return dao.searchAll(con, bean);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UpdateException("게시글 검색 중 오류 발생");
		} finally {
			DBUtil.close(con);
		}
	}
}
