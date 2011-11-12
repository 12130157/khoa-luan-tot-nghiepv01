package uit.cnpm02.dkhp.access.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uit.cnpm02.dkhp.dto.BanTin;

/**
 *
 * @author LocNguyen
 */
public class BanTinMapper extends MapperDb {
    private final static String TABLE_NAME = "BanTin";
    private final static String MATIN = "MaTin";
    private final static String TIEUDE = "TieuDe";
    private final static String NOIDUNG = "NoiDung";
    private final static String NGUOIDANG = "NguoiDang";
    private final static String NGAYDANG = "NgayTao";
    private final static String LOAI = "Loai";

    public BanTinMapper() throws Exception {
        super();
    }
    
    public void Add(BanTin news) throws Exception {
        
    }
    
    public void Add(Collection<BanTin> news) throws Exception {
        
    }

    public List<BanTin> getAll() throws Exception{
        List<BanTin> listResult = new ArrayList<BanTin>();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Select DISTINCT * from " + MapperConstant.DB_NAME + "." + TABLE_NAME + "BanTin");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while ((rs != null) && rs.next()) {
                BanTin news = new BanTin();
                IniBanTinDTOFromRs(news, rs);
                listResult.add(news);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return listResult;
    }

    public void IniBanTinDTOFromRs(BanTin news, ResultSet rs) throws SQLException {
        if ((rs != null) && (news != null)) {
            news.setMaTin(rs.getString(MATIN));
            news.setTieuDe(rs.getString(TIEUDE));
            news.setNoiDung(rs.getString(NOIDUNG));
            news.setLoai(rs.getInt(LOAI));
            news.setNgayTao(rs.getDate(NGAYDANG));
            news.setNguoiDang(rs.getString(NGUOIDANG));
        }
    }
}
