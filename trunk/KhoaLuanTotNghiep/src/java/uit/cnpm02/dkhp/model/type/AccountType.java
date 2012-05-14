/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model.type;

/**
 *
 * @author LocNguyen
 */
public enum AccountType {
        ADMIN(1, "Admin"),
        LECTUTER(2, "Giảng viên"),
        STUDENT(3, "Sinh viên");
        
        
        private int value;
        private String description;
        
        AccountType(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        public int value() {
            return this.value;
        }
        
        public String description() {
            return this.description;
        }
        
        public static String getDescription(int value) {
            if (value == ADMIN.value()) {
                return ADMIN.description();
            } else if(value == LECTUTER.value()) {
                return LECTUTER.description();
            } else if (value == STUDENT.value()) {
                return STUDENT.description();
            }
            return "KXĐ";
                
        }
        
    }
