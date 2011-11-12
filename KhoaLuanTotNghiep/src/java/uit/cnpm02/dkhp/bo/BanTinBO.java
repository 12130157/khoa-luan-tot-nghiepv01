package uit.cnpm02.dkhp.bo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.access.mapper.BanTinMapper;
import uit.cnpm02.dkhp.dto.BanTin;

/**
 *
 * @author LocNguyen
 */
public class BanTinBO {

    private BanTinMapper mapper;

    public BanTinBO() {
        try {
            mapper = new BanTinMapper();
        } catch (Exception ex) {
            Logger.getLogger(BanTinBO.class.getName()).log(Level.SEVERE, "Can not create mapper to BanTin Mapper");
        }
    }

    public List<BanTin> getAll() throws Exception {
        return mapper.getAll();
    }
}
