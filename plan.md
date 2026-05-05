# Kế Hoạch Dự Án: Banking App

**Stack:** Java (Spring Boot) · SQL Server (T-SQL / SSMS) · React (Vite + Ant Design) · Android Studio (Java)  
**Mục tiêu:** Hoàn thiện Web App trước → Mobile App sau

---

## Tổng Quan Các Giai Đoạn

| Giai đoạn | Nội dung | Ưu tiên |
|---|---|---|
| 1. Phân tích & Thiết kế | Xác định chức năng, thiết kế DB | Đầu tiên |
| 2. Setup môi trường | Cài đặt công cụ, khởi tạo project | Đầu tiên |
| 3. Database | Toàn bộ T-SQL trên SSMS | Song song với Backend |
| 4. Backend | Spring Boot REST API | Sau DB |
| 5. Frontend Web | React + Ant Design | Sau Backend |
| 6. Kiểm thử & Hoàn thiện | Test, fix bug, tài liệu | Cuối Web App |
| 7. Mobile App | Android Studio Java | Sau Web App hoàn thiện |

> Ghi chú: đây là kế hoạch làm sản phẩm hoàn chỉnh, nhưng phần CSDL sẽ được đầu tư sâu hơn để phù hợp với môn học.

---

## GIAI ĐOẠN 1 — Phân Tích & Thiết Kế

### Task 1.1 — Danh Sách Chức Năng & Use Case

> **Phân loại ưu tiên:**
> - 🔴 **MVP** — Bắt buộc có trong phiên bản đầu
> - 🟡 **Phase 2** — Nên có, làm sau MVP
> - 🟢 **Phase 3** — Nâng cao, làm cuối hoặc bỏ qua nếu không đủ thời gian

---

#### NHÓM 1: AUTHENTICATION & BẢO MẬT TÀI KHOẢN

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 1.1 | Đăng ký tài khoản | Khách hàng điền thông tin cá nhân, CCCD, SĐT để tạo tài khoản | 🔴 MVP |
| 1.2 | Đăng nhập bằng username/password | Khách hàng nhập thông tin đăng nhập | 🔴 MVP |
| 1.3 | Đăng nhập bằng PIN | Khách hàng dùng mã PIN 6 số thay password | 🟡 Phase 2 |
| 1.4 | Đăng nhập bằng sinh trắc học | Vân tay / FaceID (mobile) | 🟡 Phase 2 |
| 1.5 | Xác thực 2 yếu tố (2FA) | OTP gửi qua SMS hoặc Email khi đăng nhập | 🟡 Phase 2 |
| 1.6 | Đăng xuất | Khách hàng thoát phiên đăng nhập | 🔴 MVP |
| 1.7 | Quên mật khẩu | Reset password qua email/SĐT xác thực | 🟡 Phase 2 |
| 1.8 | Đổi mật khẩu | Khách hàng thay đổi password khi đã đăng nhập | 🔴 MVP |
| 1.9 | Đổi mã PIN | Thay đổi PIN giao dịch | 🟡 Phase 2 |
| 1.10 | Khóa tài khoản tạm thời | Tự khóa khi nghi ngờ bị lộ thông tin | 🟡 Phase 2 |
| 1.11 | Quản lý phiên đăng nhập | Xem danh sách thiết bị đang đăng nhập, đăng xuất từ xa | 🟢 Phase 3 |
| 1.12 | Xác thực OTP giao dịch | Nhập OTP trước khi thực hiện giao dịch lớn | 🟡 Phase 2 |

---

#### NHÓM 2: QUẢN LÝ TÀI KHOẢN

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 2.1 | Xem danh sách tài khoản | Hiển thị tất cả tài khoản của khách hàng | 🔴 MVP |
| 2.2 | Xem số dư tài khoản | Xem số dư hiện tại, số dư khả dụng | 🔴 MVP |
| 2.3 | Ẩn/hiện số dư | Toggle ẩn số tiền để bảo mật | 🟡 Phase 2 |
| 2.4 | Mở tài khoản thanh toán | Tạo thêm tài khoản thanh toán mới | 🟡 Phase 2 |
| 2.5 | Mở tài khoản tiết kiệm | Chọn kỳ hạn, lãi suất và gửi tiết kiệm | 🟡 Phase 2 |
| 2.6 | Xem thông tin chi tiết tài khoản | Số tài khoản, loại, ngày mở, lãi suất | 🔴 MVP |
| 2.7 | Đặt tài khoản mặc định | Chọn tài khoản mặc định cho giao dịch | 🟡 Phase 2 |
| 2.8 | Đóng tài khoản | Yêu cầu đóng tài khoản (cần duyệt) | 🟡 Phase 2 |
| 2.9 | Xem số tài khoản dạng QR | Generate QR code từ số tài khoản | 🟡 Phase 2 |
| 2.10 | Sao chép số tài khoản | Copy nhanh số tài khoản | 🟡 Phase 2 |
| 2.11 | Xem lãi suất tiết kiệm | Tra cứu bảng lãi suất các kỳ hạn | 🟡 Phase 2 |
| 2.12 | Tất toán tài khoản tiết kiệm | Rút tiền trước hạn (chịu phạt lãi suất) | 🟢 Phase 3 |

---

#### NHÓM 3: GIAO DỊCH

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 3.1 | Chuyển tiền nội bộ | Chuyển giữa 2 tài khoản cùng ngân hàng | 🔴 MVP |
| 3.2 | Chuyển tiền liên ngân hàng | Chuyển sang ngân hàng khác qua Napas/CITAD | 🟡 Phase 2 |
| 3.3 | Chuyển tiền theo số tài khoản | Nhập số tài khoản thủ công | 🔴 MVP |
| 3.4 | Chuyển tiền theo QR Code | Quét QR để điền thông tin tự động | 🟡 Phase 2 |
| 3.5 | Chuyển tiền theo SĐT | Chuyển bằng số điện thoại (nếu đã liên kết) | 🟡 Phase 2 |
| 3.6 | Nạp tiền vào tài khoản | Mô phỏng nạp tiền (ATM/quầy) | 🔴 MVP |
| 3.7 | Rút tiền từ tài khoản | Mô phỏng yêu cầu rút tiền | 🔴 MVP |
| 3.8 | Chuyển tiền đặt lịch | Hẹn giờ thực hiện giao dịch trong tương lai | 🟢 Phase 3 |
| 3.9 | Chuyển tiền định kỳ | Tự động chuyển tiền lặp lại theo chu kỳ | 🟢 Phase 3 |
| 3.10 | Xác nhận thông tin trước khi chuyển | Màn hình review trước khi submit | 🔴 MVP |
| 3.11 | Nhập mô tả giao dịch | Ghi chú nội dung chuyển khoản | 🔴 MVP |
| 3.12 | Giới hạn giao dịch hàng ngày | Cài đặt hạn mức tối đa/ngày | 🟡 Phase 2 |
| 3.13 | Hủy giao dịch đang chờ | Hủy lệnh chuyển tiền chưa xử lý | 🟡 Phase 2 |

---

#### NHÓM 4: LỊCH SỬ GIAO DỊCH

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 4.1 | Xem lịch sử giao dịch | Danh sách tất cả giao dịch theo thời gian | 🔴 MVP |
| 4.2 | Lọc theo khoảng thời gian | Chọn ngày bắt đầu, kết thúc | 🔴 MVP |
| 4.3 | Lọc theo loại giao dịch | Chuyển tiền, nạp, rút, phí | 🔴 MVP |
| 4.4 | Lọc theo trạng thái | Thành công, thất bại, đang xử lý | 🟡 Phase 2 |
| 4.5 | Tìm kiếm giao dịch | Tìm theo số tiền, tên người nhận, mô tả | 🟡 Phase 2 |
| 4.6 | Xem chi tiết giao dịch | Mã GD, thời gian, phí, trạng thái đầy đủ | 🔴 MVP |
| 4.7 | Xuất sao kê PDF | Tải file PDF sao kê theo kỳ | 🟡 Phase 2 |
| 4.8 | Xuất sao kê Excel | Tải file Excel để phân tích | 🟡 Phase 2 |
| 4.9 | Chia sẻ biên lai giao dịch | Share ảnh biên lai qua Zalo/Facebook | 🟢 Phase 3 |
| 4.10 | Báo cáo giao dịch theo tháng | Tổng thu/chi theo từng tháng dạng biểu đồ | 🟡 Phase 2 |

---

#### NHÓM 5: DANH BẠ NGƯỜI THỤ HƯỞNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 5.1 | Thêm người thụ hưởng | Lưu số tài khoản + tên ngân hàng | 🟡 Phase 2 |
| 5.2 | Xem danh sách người thụ hưởng | Hiển thị danh bạ đã lưu | 🟡 Phase 2 |
| 5.3 | Xóa người thụ hưởng | Xoá khỏi danh sách | 🟡 Phase 2 |
| 5.4 | Sửa tên gợi nhớ | Đặt nickname cho người thụ hưởng | 🟡 Phase 2 |
| 5.5 | Chuyển tiền nhanh từ danh bạ | Chọn từ danh bạ → điền sẵn thông tin | 🟡 Phase 2 |
| 5.6 | Tự động lưu sau giao dịch | Gợi ý lưu người nhận sau lần đầu chuyển | 🟢 Phase 3 |

---

#### NHÓM 6: THÔNG BÁO

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 6.1 | Thông báo biến động số dư | Push notification khi có tiền vào/ra | 🔴 MVP |
| 6.2 | Thông báo giao dịch thành công | Xác nhận sau khi chuyển tiền xong | 🔴 MVP |
| 6.3 | Thông báo giao dịch thất bại | Cảnh báo khi lỗi | 🔴 MVP |
| 6.4 | Thông báo đăng nhập lạ | Alert khi đăng nhập từ thiết bị mới | 🟡 Phase 2 |
| 6.5 | Thông báo hệ thống | Bảo trì, cập nhật chính sách | 🟡 Phase 2 |
| 6.6 | Xem danh sách thông báo | Lịch sử tất cả thông báo | 🔴 MVP |
| 6.7 | Đánh dấu đã đọc | Đọc/chưa đọc từng thông báo | 🟡 Phase 2 |
| 6.8 | Cài đặt loại thông báo | Bật/tắt từng loại thông báo | 🟢 Phase 3 |
| 6.9 | Thông báo sắp đến hạn tiết kiệm | Nhắc trước khi tài khoản TK đáo hạn | 🟢 Phase 3 |

---

#### NHÓM 7: HỒ SƠ KHÁCH HÀNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 7.1 | Xem thông tin cá nhân | Họ tên, CCCD, SĐT, địa chỉ, ngày sinh | 🔴 MVP |
| 7.2 | Cập nhật địa chỉ | Sửa địa chỉ thường trú | 🟡 Phase 2 |
| 7.3 | Cập nhật email | Đổi email liên hệ | 🟡 Phase 2 |
| 7.4 | Cập nhật ảnh đại diện | Upload ảnh profile | 🟢 Phase 3 |
| 7.5 | Xem hạng khách hàng | Standard / Silver / Gold / Platinum | 🟢 Phase 3 |
| 7.6 | Xem điểm thưởng | Điểm tích lũy từ giao dịch | 🟢 Phase 3 |
| 7.7 | Xem thông tin eKYC | Trạng thái xác thực danh tính | 🟡 Phase 2 |

---

#### NHÓM 8: THANH TOÁN & DỊCH VỤ

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 8.1 | Thanh toán hóa đơn điện | Nhập mã khách hàng, thanh toán tiền điện | 🟡 Phase 2 |
| 8.2 | Thanh toán hóa đơn nước | Thanh toán tiền nước | 🟡 Phase 2 |
| 8.3 | Thanh toán internet/cáp | Thanh toán FPT, VNPT, Viettel | 🟡 Phase 2 |
| 8.4 | Nạp tiền điện thoại | Nạp thẻ cho số di động | 🟡 Phase 2 |
| 8.5 | Thanh toán học phí | Nhập mã trường, mã sinh viên | 🟡 Phase 2 |
| 8.6 | Thanh toán thẻ tín dụng | Trả nợ thẻ tín dụng | 🟡 Phase 2 |
| 8.7 | Thanh toán khoản vay | Trả góp khoản vay ngân hàng | 🟡 Phase 2 |
| 8.8 | Thanh toán bảo hiểm | Đóng phí bảo hiểm định kỳ | 🟢 Phase 3 |
| 8.9 | Lưu hóa đơn thường dùng | Lưu lại để thanh toán nhanh lần sau | 🟡 Phase 2 |
| 8.10 | Lịch sử thanh toán hóa đơn | Tra cứu các lần đã thanh toán | 🟡 Phase 2 |

---

#### NHÓM 9: THẺ NGÂN HÀNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 9.1 | Xem danh sách thẻ | Thẻ ATM, thẻ tín dụng đang có | 🟡 Phase 2 |
| 9.2 | Xem thông tin thẻ | Số thẻ (ẩn), ngày hết hạn, trạng thái | 🟡 Phase 2 |
| 9.3 | Khóa thẻ tạm thời | Khóa khi mất thẻ hoặc nghi ngờ | 🟡 Phase 2 |
| 9.4 | Mở khóa thẻ | Mở lại thẻ đã khóa | 🟡 Phase 2 |
| 9.5 | Đặt hạn mức chi tiêu thẻ | Giới hạn số tiền được dùng/ngày | 🟡 Phase 2 |
| 9.6 | Bật/tắt thanh toán quốc tế | Cho phép dùng thẻ ngoài nước | 🟢 Phase 3 |
| 9.7 | Bật/tắt thanh toán online | Cho phép dùng thẻ mua hàng online | 🟢 Phase 3 |
| 9.8 | Yêu cầu cấp thẻ mới | Đăng ký mở thẻ ATM/tín dụng | 🟡 Phase 2 |
| 9.9 | Báo cáo thẻ thất lạc | Yêu cầu khóa và cấp thẻ mới | 🟡 Phase 2 |
| 9.10 | Xem sao kê thẻ tín dụng | Danh sách giao dịch thẻ tín dụng | 🟡 Phase 2 |
| 9.11 | Trả nợ thẻ tín dụng | Thanh toán tối thiểu hoặc toàn bộ dư nợ | 🟡 Phase 2 |

---

#### NHÓM 10: QUẢN LÝ TÀI CHÍNH CÁ NHÂN

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 10.1 | Thống kê thu/chi theo tháng | Biểu đồ tròn/cột phân loại chi tiêu | 🟢 Phase 3 |
| 10.2 | Phân loại giao dịch | Ăn uống, mua sắm, giải trí, hóa đơn | 🟢 Phase 3 |
| 10.3 | Đặt ngân sách theo danh mục | Giới hạn chi tiêu mỗi loại/tháng | 🟢 Phase 3 |
| 10.4 | Cảnh báo vượt ngân sách | Thông báo khi gần đạt hạn mức | 🟢 Phase 3 |
| 10.5 | Mục tiêu tiết kiệm | Đặt mục tiêu số tiền, theo dõi tiến độ | 🟢 Phase 3 |
| 10.6 | Tổng quan tài chính | Dashboard tổng tài sản, nợ, tiết kiệm | 🟢 Phase 3 |

---

#### NHÓM 11: ADMIN — QUẢN LÝ NGƯỜI DÙNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 11.1 | Xem danh sách khách hàng | Tìm kiếm, lọc theo trạng thái | 🔴 MVP |
| 11.2 | Xem chi tiết khách hàng | Thông tin cá nhân, tài khoản, lịch sử GD | 🔴 MVP |
| 11.3 | Tạo tài khoản khách hàng | Admin tạo thay cho khách đến quầy | 🔴 MVP |
| 11.4 | Kích hoạt / Vô hiệu hóa tài khoản | Bật/tắt trạng thái tài khoản người dùng | 🔴 MVP |
| 11.5 | Reset mật khẩu khách hàng | Admin reset khi khách quên mật khẩu | 🟡 Phase 2 |
| 11.6 | Xem lịch sử đăng nhập | IP, thiết bị, thời gian đăng nhập | 🟡 Phase 2 |
| 11.7 | Phân quyền nhân viên | Gán role Employee / Admin | 🔴 MVP |

---

#### NHÓM 12: ADMIN — QUẢN LÝ TÀI KHOẢN NGÂN HÀNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 12.1 | Xem tất cả tài khoản | Tìm kiếm theo số TK, khách hàng | 🔴 MVP |
| 12.2 | Khóa / Mở khóa tài khoản | Khóa tài khoản vi phạm | 🔴 MVP |
| 12.3 | Điều chỉnh số dư | Chỉnh thủ công (có ghi audit log) | 🟡 Phase 2 |
| 12.4 | Xem số dư toàn hệ thống | Tổng tiền gửi đang quản lý | 🟡 Phase 2 |
| 12.5 | Đóng tài khoản | Duyệt yêu cầu đóng TK của khách | 🟡 Phase 2 |

---

#### NHÓM 13: ADMIN — QUẢN LÝ GIAO DỊCH

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 13.1 | Xem tất cả giao dịch | Danh sách toàn hệ thống | 🔴 MVP |
| 13.2 | Tìm kiếm giao dịch | Theo mã GD, số TK, số tiền, thời gian | 🔴 MVP |
| 13.3 | Xem chi tiết giao dịch | Đầy đủ thông tin kỹ thuật | 🔴 MVP |
| 13.4 | Duyệt giao dịch đang chờ | Approve giao dịch lớn cần xét duyệt | 🟡 Phase 2 |
| 13.5 | Từ chối giao dịch | Reject kèm lý do | 🟡 Phase 2 |
| 13.6 | Hoàn tiền giao dịch | Reverse một giao dịch đã xử lý | 🟡 Phase 2 |
| 13.7 | Cờ giao dịch đáng ngờ | Đánh dấu để điều tra thêm | 🟢 Phase 3 |
| 13.8 | Xuất báo cáo giao dịch | Excel/PDF theo ngày, tuần, tháng | 🟡 Phase 2 |

---

#### NHÓM 14: ADMIN — BÁO CÁO & THỐNG KÊ

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 14.1 | Dashboard tổng quan | Số KH, tổng GD, tổng tiền hôm nay | 🔴 MVP |
| 14.2 | Báo cáo doanh thu phí | Tổng phí thu được theo kỳ | 🟡 Phase 2 |
| 14.3 | Báo cáo giao dịch theo loại | Phân tích chuyển tiền vs nạp vs rút | 🟡 Phase 2 |
| 14.4 | Biểu đồ tăng trưởng KH | Số lượng KH mới theo tháng | 🟡 Phase 2 |
| 14.5 | Top tài khoản giao dịch nhiều nhất | Xếp hạng theo số lượng / giá trị GD | 🟢 Phase 3 |
| 14.6 | Báo cáo giao dịch thất bại | Tỷ lệ lỗi, phân tích nguyên nhân | 🟡 Phase 2 |

---

#### NHÓM 15: ADMIN — BẢO MẬT & AUDIT

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 15.1 | Xem audit log | Mọi hành động trong hệ thống đều ghi log | 🔴 MVP |
| 15.2 | Xem log đăng nhập thất bại | Phát hiện brute-force attack | 🟡 Phase 2 |
| 15.3 | Cấu hình giới hạn giao dịch | Hạn mức tối đa theo loại GD | 🟡 Phase 2 |
| 15.4 | Cấu hình thời gian hết phiên | Session timeout | 🟡 Phase 2 |
| 15.5 | Xem cảnh báo bảo mật | Danh sách tài khoản bị khóa do sai PIN | 🟡 Phase 2 |
| 15.6 | Blacklist IP | Chặn IP nghi ngờ tấn công | 🟢 Phase 3 |

---

#### NHÓM 16: CÀI ĐẶT ỨNG DỤNG

| # | Chức năng | Use Case | Ưu tiên |
|---|---|---|---|
| 16.1 | Đổi ngôn ngữ | Tiếng Việt / Tiếng Anh | 🟢 Phase 3 |
| 16.2 | Đổi giao diện sáng/tối | Light / Dark mode | 🟢 Phase 3 |
| 16.3 | Cài đặt thông báo | Bật/tắt từng loại push notification | 🟡 Phase 2 |
| 16.4 | Xem phiên bản ứng dụng | App version, hotline hỗ trợ | 🟡 Phase 2 |
| 16.5 | Điều khoản sử dụng | Hiển thị Terms & Conditions | 🟡 Phase 2 |
| 16.6 | Chính sách bảo mật | Privacy policy | 🟡 Phase 2 |
| 16.7 | Liên hệ hỗ trợ | Số hotline, chat trực tiếp | 🟡 Phase 2 |
| 16.8 | FAQ | Câu hỏi thường gặp | 🟢 Phase 3 |

---

### Task 1.2 — Thiết kế Entity và Database Schema

> ERD sẽ được tạo trong giai đoạn phân tích để chốt mô hình, sau đó cập nhật lại khi hoàn thành script ở Giai đoạn 3.

**Nội dung cần làm cho phần CSDL:**
- Phân tích thực thể, thuộc tính, quan hệ và ràng buộc
- Chuẩn hóa lược đồ đến mức phù hợp
- Thiết kế lược đồ logic và lược đồ vật lý
- Lập data dictionary cho từng bảng và cột chính
- Xác định các chỉ mục cần có và mục đích sử dụng
- Chuẩn bị seed data, backup/restore và kiểm tra truy vấn

**Các thực thể chính:**

| Thực thể | Mô tả |
|---|---|
| `users` | Thông tin đăng nhập, role |
| `customers` | Thông tin khách hàng (1-1 với users) |
| `accounts` | Tài khoản ngân hàng |
| `cards` | Thẻ ngân hàng liên kết tài khoản |
| `transactions` | Lịch sử giao dịch |
| `beneficiaries` | Danh bạ người thụ hưởng |
| `notifications` | Thông báo gửi đến khách hàng |
| `bill_payments` | Lịch sử thanh toán hóa đơn |
| `saved_bills` | Hóa đơn thường dùng đã lưu |
| `audit_logs` | Log thao tác hệ thống |
| `login_history` | Lịch sử đăng nhập |
| `interest_rates` | Bảng lãi suất tiết kiệm |
| `system_config` | Cấu hình hệ thống (hạn mức, timeout...) |

---

### Task 1.3 — Thiết kế API (Backend Contract)

> API endpoint sẽ được định nghĩa đầy đủ sau khi hoàn thành Database Schema và Backend (Giai đoạn 3-4). Dùng Swagger để sinh tài liệu tự động.

---

## GIAI ĐOẠN 2 — Setup Môi Trường

### Task 2.1 — Cài đặt công cụ

**Cách làm — Lần lượt cài đặt:**

```
Backend:
- JDK 17 hoặc 21 (LTS)
- IntelliJ IDEA (Community hoặc Ultimate)
- Maven 3.x (thường đi kèm IntelliJ)

Database:
- SQL Server 2022 Developer Edition (miễn phí)
- SQL Server Management Studio (SSMS) - bản mới nhất
- Hoặc: Docker Desktop + image mcr.microsoft.com/mssql/server

Frontend:
- Node.js LTS (v20+)
- VS Code + extensions: ES7 React, Prettier, ESLint
- Git

Testing:
- Postman (test API)
```

---

### Task 2.2 — Khởi tạo project Backend (Spring Boot)

**Cách làm:**
1. Truy cập [https://start.spring.io](https://start.spring.io)
2. Cấu hình:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.x
   - Packaging: Jar
   - Java: 17
3. Chọn Dependencies:
   - Spring Web
   - Spring Data JPA
   - Spring Security
   - MS SQL Server Driver
   - Lombok
   - Validation
4. Generate → mở bằng IntelliJ IDEA
5. Cấu hình `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=banking_db;encrypt=false
spring.datasource.username=sa
spring.datasource.password=YourPassword
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

**Cấu trúc package đề xuất:**
```
com.banking
├── config/         # Security config, JWT config
├── controller/     # REST Controllers
├── service/        # Business logic
├── repository/     # JPA Repositories
├── entity/         # JPA Entities
├── dto/            # Request/Response DTOs
├── exception/      # Custom exceptions
└── util/           # Utility classes
```

---

### Task 2.3 — Khởi tạo project Frontend (React + Vite)

**Cách làm:**

```bash
npm create vite@latest banking-frontend -- --template react
cd banking-frontend
npm install

# Cài thêm các thư viện cần thiết
npm install antd @ant-design/icons
npm install axios
npm install react-router-dom
npm install @tanstack/react-query
npm install zustand
npm install dayjs
```

**Cấu trúc thư mục đề xuất:**
```
src/
├── api/            # Axios instance + API calls
├── components/     # Shared UI components
├── pages/          # Trang: Login, Dashboard, Transfer...
├── hooks/          # Custom hooks
├── store/          # Zustand state management
├── utils/          # Helper functions
└── App.jsx
```

---

## GIAI ĐOẠN 3 — Database (T-SQL trên SSMS)

### Mục tiêu của giai đoạn này

- Hoàn thiện schema CSDL phục vụ app
- Có migration rõ ràng up/down
- Có seed data để demo
- Có index, backup/restore và bằng chứng tối ưu truy vấn
- Có stored procedure cho các nghiệp vụ chính của ngân hàng

### Task 3.1 — Tạo Database và Migration Scripts

**Hướng thực hiện:** Viết toàn bộ bằng T-SQL thuần trên SSMS. Tổ chức theo từng file migration đánh số thứ tự.

**Tạo database:**
```sql
CREATE DATABASE banking_db
COLLATE Vietnamese_CI_AS;
GO
USE banking_db;
GO
```

---

#### V001 — Bảng `users`

**UP:**
```sql
CREATE TABLE users (
    user_id     INT IDENTITY(1,1)   PRIMARY KEY,
    username    NVARCHAR(50)        NOT NULL UNIQUE,
    password    NVARCHAR(255)       NOT NULL,  -- BCrypt hash
    email       NVARCHAR(100)       NOT NULL UNIQUE,
    role        NVARCHAR(20)        NOT NULL DEFAULT 'CUSTOMER'
                CONSTRAINT chk_role CHECK (role IN ('CUSTOMER','EMPLOYEE','ADMIN')),
    is_active   BIT                 NOT NULL DEFAULT 1,
    created_at  DATETIME2           NOT NULL DEFAULT GETDATE(),
    updated_at  DATETIME2           NOT NULL DEFAULT GETDATE()
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS users;
GO
```

---

#### V002 — Bảng `customers`

**UP:**
```sql
CREATE TABLE customers (
    customer_id     INT IDENTITY(1,1)   PRIMARY KEY,
    user_id         INT                 NOT NULL UNIQUE,
    full_name       NVARCHAR(100)       NOT NULL,
    id_number       NVARCHAR(20)        NOT NULL UNIQUE,  -- CMND/CCCD
    phone           NVARCHAR(15)        NOT NULL UNIQUE,
    date_of_birth   DATE                NOT NULL,
    address         NVARCHAR(255)       NULL,
    avatar_url      NVARCHAR(500)       NULL,
    kyc_status      NVARCHAR(20)        NOT NULL DEFAULT 'PENDING'
                    CONSTRAINT chk_kyc CHECK (kyc_status IN ('PENDING','VERIFIED','REJECTED')),
    CONSTRAINT fk_customers_users FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS customers;
GO
```

---

#### V003 — Bảng `accounts`

**UP:**
```sql
CREATE TABLE accounts (
    account_id      INT IDENTITY(1,1)       PRIMARY KEY,
    account_number  NVARCHAR(20)            NOT NULL UNIQUE,
    customer_id     INT                     NOT NULL,
    account_type    NVARCHAR(20)            NOT NULL DEFAULT 'PAYMENT'
                    CONSTRAINT chk_acc_type CHECK (account_type IN ('PAYMENT','SAVINGS')),
    balance         DECIMAL(18,2)           NOT NULL DEFAULT 0.00,
    currency        NCHAR(3)                NOT NULL DEFAULT 'VND',
    status          NVARCHAR(20)            NOT NULL DEFAULT 'ACTIVE'
                    CONSTRAINT chk_acc_status CHECK (status IN ('ACTIVE','LOCKED','CLOSED')),
    is_default      BIT                     NOT NULL DEFAULT 0,
    daily_limit     DECIMAL(18,2)           NOT NULL DEFAULT 100000000.00,
    opened_at       DATETIME2               NOT NULL DEFAULT GETDATE(),
    closed_at       DATETIME2               NULL,
    -- Chỉ dùng cho tài khoản tiết kiệm
    term_months     INT                     NULL,
    interest_rate   DECIMAL(5,2)            NULL,
    maturity_date   DATE                    NULL,
    CONSTRAINT fk_accounts_customers FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT chk_balance CHECK (balance >= 0)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS accounts;
GO
```

---

#### V004 — Bảng `transactions`

**UP:**
```sql
CREATE TABLE transactions (
    transaction_id      INT IDENTITY(1,1)       PRIMARY KEY,
    transaction_code    NVARCHAR(30)            NOT NULL UNIQUE,
    from_account_id     INT                     NULL,
    to_account_id       INT                     NULL,
    amount              DECIMAL(18,2)           NOT NULL,
    fee                 DECIMAL(18,2)           NOT NULL DEFAULT 0.00,
    transaction_type    NVARCHAR(20)            NOT NULL
                        CONSTRAINT chk_tx_type CHECK (
                            transaction_type IN ('TRANSFER','DEPOSIT','WITHDRAWAL','FEE','PAYMENT','REVERSAL')
                        ),
    status              NVARCHAR(20)            NOT NULL DEFAULT 'PENDING'
                        CONSTRAINT chk_tx_status CHECK (
                            status IN ('PENDING','SUCCESS','FAILED','CANCELLED','REVERSED')
                        ),
    description         NVARCHAR(255)           NULL,
    reference_code      NVARCHAR(50)            NULL,
    is_suspicious       BIT                     NOT NULL DEFAULT 0,
    created_at          DATETIME2               NOT NULL DEFAULT GETDATE(),
    completed_at        DATETIME2               NULL,
    CONSTRAINT fk_tx_from FOREIGN KEY (from_account_id) REFERENCES accounts(account_id),
    CONSTRAINT fk_tx_to   FOREIGN KEY (to_account_id)   REFERENCES accounts(account_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS transactions;
GO
```

---

#### V005 — Bảng `beneficiaries`

**UP:**
```sql
CREATE TABLE beneficiaries (
    beneficiary_id      INT IDENTITY(1,1)   PRIMARY KEY,
    customer_id         INT                 NOT NULL,
    account_number      NVARCHAR(20)        NOT NULL,
    bank_name           NVARCHAR(100)       NOT NULL DEFAULT N'Nội bộ',
    full_name           NVARCHAR(100)       NOT NULL,
    nickname            NVARCHAR(50)        NULL,
    is_active           BIT                 NOT NULL DEFAULT 1,
    created_at          DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_beneficiaries_customers FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS beneficiaries;
GO
```

---

#### V006 — Bảng `cards`

**UP:**
```sql
CREATE TABLE cards (
    card_id             INT IDENTITY(1,1)   PRIMARY KEY,
    card_number         NVARCHAR(20)        NOT NULL UNIQUE,
    account_id          INT                 NOT NULL,
    card_type           NVARCHAR(20)        NOT NULL
                        CONSTRAINT chk_card_type CHECK (card_type IN ('DEBIT','CREDIT')),
    status              NVARCHAR(20)        NOT NULL DEFAULT 'ACTIVE'
                        CONSTRAINT chk_card_status CHECK (status IN ('ACTIVE','LOCKED','EXPIRED','CANCELLED')),
    expiry_date         DATE                NOT NULL,
    daily_limit         DECIMAL(18,2)       NOT NULL DEFAULT 50000000.00,
    allow_international BIT                 NOT NULL DEFAULT 0,
    allow_online        BIT                 NOT NULL DEFAULT 1,
    issued_at           DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_cards_accounts FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS cards;
GO
```

---

#### V007 — Bảng `notifications`

**UP:**
```sql
CREATE TABLE notifications (
    notification_id     INT IDENTITY(1,1)   PRIMARY KEY,
    user_id             INT                 NOT NULL,
    title               NVARCHAR(200)       NOT NULL,
    content             NVARCHAR(1000)      NOT NULL,
    type                NVARCHAR(30)        NOT NULL
                        CONSTRAINT chk_notif_type CHECK (
                            type IN ('TRANSACTION','SECURITY','SYSTEM','PROMOTION','REMINDER')
                        ),
    is_read             BIT                 NOT NULL DEFAULT 0,
    related_tx_id       INT                 NULL,
    created_at          DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_notifications_users FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_notifications_tx FOREIGN KEY (related_tx_id) REFERENCES transactions(transaction_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS notifications;
GO
```

---

#### V008 — Bảng `bill_payments`

**UP:**
```sql
CREATE TABLE bill_payments (
    bill_payment_id     INT IDENTITY(1,1)   PRIMARY KEY,
    customer_id         INT                 NOT NULL,
    from_account_id     INT                 NOT NULL,
    bill_type           NVARCHAR(30)        NOT NULL
                        CONSTRAINT chk_bill_type CHECK (
                            bill_type IN ('ELECTRICITY','WATER','INTERNET','PHONE','TUITION','INSURANCE','CREDIT_CARD','LOAN')
                        ),
    provider_name       NVARCHAR(100)       NOT NULL,
    customer_code       NVARCHAR(50)        NOT NULL,
    amount              DECIMAL(18,2)       NOT NULL,
    status              NVARCHAR(20)        NOT NULL DEFAULT 'SUCCESS',
    transaction_id      INT                 NULL,
    paid_at             DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_billpay_customers FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_billpay_accounts FOREIGN KEY (from_account_id) REFERENCES accounts(account_id),
    CONSTRAINT fk_billpay_tx FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS bill_payments;
GO
```

---

#### V009 — Bảng `saved_bills`

**UP:**
```sql
CREATE TABLE saved_bills (
    saved_bill_id       INT IDENTITY(1,1)   PRIMARY KEY,
    customer_id         INT                 NOT NULL,
    bill_type           NVARCHAR(30)        NOT NULL,
    provider_name       NVARCHAR(100)       NOT NULL,
    customer_code       NVARCHAR(50)        NOT NULL,
    nickname            NVARCHAR(100)       NULL,
    is_active           BIT                 NOT NULL DEFAULT 1,
    created_at          DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_savedbills_customers FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS saved_bills;
GO
```

---

#### V010 — Bảng `login_history`

**UP:**
```sql
CREATE TABLE login_history (
    login_id        INT IDENTITY(1,1)   PRIMARY KEY,
    user_id         INT                 NOT NULL,
    ip_address      NVARCHAR(50)        NULL,
    device_info     NVARCHAR(255)       NULL,
    status          NVARCHAR(20)        NOT NULL
                    CONSTRAINT chk_login_status CHECK (status IN ('SUCCESS','FAILED')),
    failure_reason  NVARCHAR(100)       NULL,
    logged_at       DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_loginhistory_users FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS login_history;
GO
```

---

#### V011 — Bảng `audit_logs`

**UP:**
```sql
CREATE TABLE audit_logs (
    log_id          INT IDENTITY(1,1)   PRIMARY KEY,
    user_id         INT                 NULL,
    action          NVARCHAR(100)       NOT NULL,
    target_table    NVARCHAR(50)        NULL,
    target_id       INT                 NULL,
    old_value       NVARCHAR(MAX)       NULL,  -- JSON snapshot trước
    new_value       NVARCHAR(MAX)       NULL,  -- JSON snapshot sau
    ip_address      NVARCHAR(50)        NULL,
    created_at      DATETIME2           NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_auditlogs_users FOREIGN KEY (user_id) REFERENCES users(user_id)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS audit_logs;
GO
```

---

#### V012 — Bảng `interest_rates`

**UP:**
```sql
CREATE TABLE interest_rates (
    rate_id         INT IDENTITY(1,1)   PRIMARY KEY,
    term_months     INT                 NOT NULL,
    rate_percent    DECIMAL(5,2)        NOT NULL,
    effective_from  DATE                NOT NULL,
    effective_to    DATE                NULL,
    is_active       BIT                 NOT NULL DEFAULT 1,
    CONSTRAINT uq_rate_term UNIQUE (term_months, effective_from)
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS interest_rates;
GO
```

---

#### V013 — Bảng `system_config`

**UP:**
```sql
CREATE TABLE system_config (
    config_id       INT IDENTITY(1,1)   PRIMARY KEY,
    config_key      NVARCHAR(100)       NOT NULL UNIQUE,
    config_value    NVARCHAR(500)       NOT NULL,
    description     NVARCHAR(255)       NULL,
    updated_at      DATETIME2           NOT NULL DEFAULT GETDATE()
);
GO
```

**DOWN:**
```sql
DROP TABLE IF EXISTS system_config;
GO
```

### Task 3.2 — Seed Data

**Mục tiêu:** Khởi tạo dữ liệu mẫu để chạy demo app và kiểm thử chức năng CSDL.

- Tạo dữ liệu mẫu cho `users`, `customers`, `accounts`, `transactions`, `notifications`
- Thêm một số tài khoản tiết kiệm và lãi suất mẫu
- Tạo một vài mẫu `beneficiaries`, `saved_bills`, `cards`
- Đảm bảo dữ liệu seed không phá vỡ ràng buộc khóa ngoại

### Task 3.3 — Tối ưu CSDL

**Mục tiêu:** Tạo các chỉ mục cần thiết và giải thích lý do dùng.

- Index cho `users.username`, `users.email`
- Index cho `customers.phone`, `customers.id_number`
- Index cho `accounts.account_number`, `accounts.customer_id`
- Index cho `transactions.created_at`, `transactions.from_account_id`, `transactions.to_account_id`
- Index cho `notifications.user_id`, `notifications.created_at`
- Chạy `EXPLAIN`/execution plan cho một số truy vấn chính để minh chứng hiệu quả

### Task 3.4 — Sao lưu, phục hồi và kiểm tra

**Mục tiêu:** Có hướng dẫn backup/restore và chứng minh hệ thống CSDL có thể khôi phục.

- Viết script backup full database
- Viết script restore khi cần kiểm thử
- Mô tả quy trình backup định kỳ và lưu trữ file backup
- Lưu lại vài truy vấn minh họa để kiểm tra sau khi restore

---

> **Thứ tự rollback toàn bộ (DOWN theo đúng thứ tự ngược FK):**
> ```sql
> -- Chạy theo thứ tự này khi cần rollback toàn bộ
> DROP TABLE IF EXISTS system_config;
> DROP TABLE IF EXISTS interest_rates;
> DROP TABLE IF EXISTS audit_logs;
> DROP TABLE IF EXISTS login_history;
> DROP TABLE IF EXISTS saved_bills;
> DROP TABLE IF EXISTS bill_payments;
> DROP TABLE IF EXISTS notifications;
> DROP TABLE IF EXISTS cards;
> DROP TABLE IF EXISTS beneficiaries;
> DROP TABLE IF EXISTS transactions;
> DROP TABLE IF EXISTS accounts;
> DROP TABLE IF EXISTS customers;
> DROP TABLE IF EXISTS users;
> GO
> ```

---

### Task 3.2 — Stored Procedures cho nghiệp vụ chính

**SP Chuyển tiền nội bộ** *(đã sửa: kiểm tra daily_limit thực sự)*:
```sql
CREATE PROCEDURE sp_transfer_money
    @from_account_number    NVARCHAR(20),
    @to_account_number      NVARCHAR(20),
    @amount                 DECIMAL(18,2),
    @description            NVARCHAR(255),
    @result_code            INT OUTPUT,
    @result_message         NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @from_id        INT;
        DECLARE @to_id          INT;
        DECLARE @from_balance   DECIMAL(18,2);
        DECLARE @daily_limit    DECIMAL(18,2);
        DECLARE @spent_today    DECIMAL(18,2);

        -- Lấy thông tin tài khoản nguồn (lock để tránh race condition)
        SELECT @from_id = account_id,
               @from_balance = balance,
               @daily_limit = daily_limit
        FROM accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @from_account_number AND status = 'ACTIVE';

        IF @from_id IS NULL BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản nguồn không tồn tại hoặc đã bị khóa';
            ROLLBACK; RETURN;
        END

        IF @amount <= 0 BEGIN
            SET @result_code = -4;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK; RETURN;
        END

        IF @from_balance < @amount BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số dư không đủ';
            ROLLBACK; RETURN;
        END

        -- Kiểm tra hạn mức giao dịch trong ngày
        SELECT @spent_today = ISNULL(SUM(amount), 0)
        FROM transactions
        WHERE from_account_id = @from_id
          AND transaction_type IN ('TRANSFER', 'WITHDRAWAL', 'PAYMENT')
          AND status = 'SUCCESS'
          AND CAST(created_at AS DATE) = CAST(GETDATE() AS DATE);

        IF (@spent_today + @amount) > @daily_limit BEGIN
            SET @result_code = -6;
            SET @result_message = N'Vượt hạn mức giao dịch trong ngày. Hạn mức còn lại: '
                + CAST(@daily_limit - @spent_today AS NVARCHAR(30)) + N' VND';
            ROLLBACK; RETURN;
        END

        -- Lấy tài khoản đích
        SELECT @to_id = account_id
        FROM accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @to_account_number AND status = 'ACTIVE';

        IF @to_id IS NULL BEGIN
            SET @result_code = -3;
            SET @result_message = N'Tài khoản đích không tồn tại hoặc đã bị khóa';
            ROLLBACK; RETURN;
        END

        IF @from_id = @to_id BEGIN
            SET @result_code = -5;
            SET @result_message = N'Không thể chuyển tiền vào chính tài khoản này';
            ROLLBACK; RETURN;
        END

        -- Thực hiện chuyển tiền
        UPDATE accounts SET balance = balance - @amount WHERE account_id = @from_id;
        UPDATE accounts SET balance = balance + @amount WHERE account_id = @to_id;

        INSERT INTO transactions (
            transaction_code, from_account_id, to_account_id,
            amount, transaction_type, status, description, completed_at
        ) VALUES (
            'TXN' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + LEFT(REPLACE(CAST(NEWID() AS NVARCHAR(36)),'-',''), 6),
            @from_id, @to_id, @amount, 'TRANSFER', 'SUCCESS', @description, GETDATE()
        );

        SET @result_code = 0;
        SET @result_message = N'Chuyển tiền thành công';
        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO
```

**SP Nạp tiền:**
```sql
CREATE PROCEDURE sp_deposit
    @account_number     NVARCHAR(20),
    @amount             DECIMAL(18,2),
    @description        NVARCHAR(255),
    @result_code        INT OUTPUT,
    @result_message     NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @account_id INT;

        SELECT @account_id = account_id
        FROM accounts WITH (UPDLOCK)
        WHERE account_number = @account_number AND status = 'ACTIVE';

        IF @account_id IS NULL BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản không tồn tại hoặc đã bị khóa';
            ROLLBACK; RETURN;
        END

        IF @amount <= 0 BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK; RETURN;
        END

        UPDATE accounts SET balance = balance + @amount WHERE account_id = @account_id;

        INSERT INTO transactions (
            transaction_code, from_account_id, to_account_id,
            amount, transaction_type, status, description, completed_at
        ) VALUES (
            'DEP' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + LEFT(REPLACE(CAST(NEWID() AS NVARCHAR(36)),'-',''), 6),
            NULL, @account_id, @amount, 'DEPOSIT', 'SUCCESS', @description, GETDATE()
        );

        SET @result_code = 0;
        SET @result_message = N'Nạp tiền thành công';
        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO
```

**SP Rút tiền:**
```sql
CREATE PROCEDURE sp_withdraw
    @account_number     NVARCHAR(20),
    @amount             DECIMAL(18,2),
    @description        NVARCHAR(255),
    @result_code        INT OUTPUT,
    @result_message     NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @account_id     INT;
        DECLARE @balance        DECIMAL(18,2);
        DECLARE @daily_limit    DECIMAL(18,2);
        DECLARE @spent_today    DECIMAL(18,2);

        SELECT @account_id = account_id,
               @balance = balance,
               @daily_limit = daily_limit
        FROM accounts WITH (UPDLOCK)
        WHERE account_number = @account_number AND status = 'ACTIVE';

        IF @account_id IS NULL BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản không tồn tại hoặc đã bị khóa';
            ROLLBACK; RETURN;
        END

        IF @amount <= 0 BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK; RETURN;
        END

        IF @balance < @amount BEGIN
            SET @result_code = -3;
            SET @result_message = N'Số dư không đủ';
            ROLLBACK; RETURN;
        END

        -- Kiểm tra hạn mức ngày
        SELECT @spent_today = ISNULL(SUM(amount), 0)
        FROM transactions
        WHERE from_account_id = @account_id
          AND transaction_type IN ('TRANSFER', 'WITHDRAWAL', 'PAYMENT')
          AND status = 'SUCCESS'
          AND CAST(created_at AS DATE) = CAST(GETDATE() AS DATE);

        IF (@spent_today + @amount) > @daily_limit BEGIN
            SET @result_code = -4;
            SET @result_message = N'Vượt hạn mức giao dịch trong ngày';
            ROLLBACK; RETURN;
        END

        UPDATE accounts SET balance = balance - @amount WHERE account_id = @account_id;

        INSERT INTO transactions (
            transaction_code, from_account_id, to_account_id,
            amount, transaction_type, status, description, completed_at
        ) VALUES (
            'WDR' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + LEFT(REPLACE(CAST(NEWID() AS NVARCHAR(36)),'-',''), 6),
            @account_id, NULL, @amount, 'WITHDRAWAL', 'SUCCESS', @description, GETDATE()
        );

        SET @result_code = 0;
        SET @result_message = N'Rút tiền thành công';
        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO
```

**SP Thanh toán hóa đơn:**
```sql
CREATE PROCEDURE sp_pay_bill
    @from_account_number    NVARCHAR(20),
    @customer_id            INT,
    @bill_type              NVARCHAR(30),
    @provider_name          NVARCHAR(100),
    @customer_code          NVARCHAR(50),
    @amount                 DECIMAL(18,2),
    @result_code            INT OUTPUT,
    @result_message         NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        DECLARE @account_id INT, @balance DECIMAL(18,2), @tx_id INT;

        SELECT @account_id = account_id, @balance = balance
        FROM accounts WITH (UPDLOCK)
        WHERE account_number = @from_account_number AND status = 'ACTIVE';

        IF @account_id IS NULL BEGIN
            SET @result_code = -1; SET @result_message = N'Tài khoản không tồn tại';
            ROLLBACK; RETURN;
        END

        IF @balance < @amount BEGIN
            SET @result_code = -2; SET @result_message = N'Số dư không đủ';
            ROLLBACK; RETURN;
        END

        UPDATE accounts SET balance = balance - @amount WHERE account_id = @account_id;

        INSERT INTO transactions (
            transaction_code, from_account_id, amount,
            transaction_type, status, description, completed_at
        ) VALUES (
            'BILL' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + LEFT(REPLACE(CAST(NEWID() AS NVARCHAR(36)),'-',''), 5),
            @account_id, @amount, 'PAYMENT', 'SUCCESS',
            N'Thanh toán ' + @bill_type + N' - ' + @provider_name, GETDATE()
        );

        SET @tx_id = SCOPE_IDENTITY();

        INSERT INTO bill_payments (
            customer_id, from_account_id, bill_type, provider_name,
            customer_code, amount, status, transaction_id
        ) VALUES (
            @customer_id, @account_id, @bill_type, @provider_name,
            @customer_code, @amount, 'SUCCESS', @tx_id
        );

        SET @result_code = 0;
        SET @result_message = N'Thanh toán hóa đơn thành công';
        COMMIT;
    END TRY
    BEGIN CATCH
        ROLLBACK;
        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO
```

---

### Task 3.3 — Seed Data (Dữ liệu mẫu)

```sql
-- =============================================
-- SEED DATA — banking_db
-- =============================================

-- 1. Lãi suất tiết kiệm
INSERT INTO interest_rates (term_months, rate_percent, effective_from) VALUES
(1,  4.50, '2024-01-01'),
(3,  5.00, '2024-01-01'),
(6,  5.50, '2024-01-01'),
(12, 6.00, '2024-01-01'),
(24, 6.20, '2024-01-01'),
(36, 6.50, '2024-01-01');

-- 2. Cấu hình hệ thống
INSERT INTO system_config (config_key, config_value, description) VALUES
('MAX_DAILY_TRANSFER',    '500000000',   N'Hạn mức chuyển tiền tối đa mỗi ngày (VND)'),
('SESSION_TIMEOUT_MIN',   '30',          N'Thời gian hết phiên (phút)'),
('MAX_LOGIN_ATTEMPTS',    '5',           N'Số lần đăng nhập sai tối đa trước khi khóa'),
('OTP_EXPIRE_MIN',        '5',           N'Thời gian hiệu lực OTP (phút)'),
('MIN_TRANSFER_AMOUNT',   '10000',       N'Số tiền chuyển tối thiểu (VND)');

-- 3. Admin account (password: Admin@123)
INSERT INTO users (username, password, email, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@bank.vn', 'ADMIN');

-- 4. Employee account (password: Employee@123)
INSERT INTO users (username, password, email, role)
VALUES ('employee01', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'employee01@bank.vn', 'EMPLOYEE');

-- 5. Customer mẫu (password: Customer@123)
INSERT INTO users (username, password, email, role) VALUES
('nguyenvana',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'vana@email.com',    'CUSTOMER'),
('tranthib',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'thib@email.com',    'CUSTOMER'),
('levanc',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'vanc@email.com',    'CUSTOMER');

INSERT INTO customers (user_id, full_name, id_number, phone, date_of_birth, address, kyc_status) VALUES
(3, N'Nguyễn Văn A', '001234567890', '0901234567', '1990-01-15', N'123 Lê Lợi, Q1, TP.HCM',  'VERIFIED'),
(4, N'Trần Thị B',   '002345678901', '0912345678', '1995-03-22', N'456 Trần Hưng Đạo, HN',   'VERIFIED'),
(5, N'Lê Văn C',     '003456789012', '0923456789', '1988-07-10', N'789 Nguyễn Huệ, Q1, HCM', 'PENDING');

INSERT INTO accounts (account_number, customer_id, account_type, balance, is_default) VALUES
('1000000001', 1, 'PAYMENT', 50000000.00,  1),
('1000000002', 1, 'SAVINGS', 100000000.00, 0),
('1000000003', 2, 'PAYMENT', 25000000.00,  1),
('1000000004', 2, 'SAVINGS', 75000000.00,  0),
('1000000005', 3, 'PAYMENT', 10000000.00,  1);

UPDATE accounts SET term_months = 6, interest_rate = 5.50, maturity_date = DATEADD(MONTH, 6, GETDATE())
WHERE account_number IN ('1000000002', '1000000004');
```

---

### Task 3.4 — Index và Tối ưu truy vấn

```sql
-- Tài khoản
CREATE INDEX idx_accounts_number      ON accounts(account_number);
CREATE INDEX idx_accounts_customer    ON accounts(customer_id);

-- Giao dịch
CREATE INDEX idx_tx_from_account      ON transactions(from_account_id, created_at DESC);
CREATE INDEX idx_tx_to_account        ON transactions(to_account_id,   created_at DESC);
CREATE INDEX idx_tx_code              ON transactions(transaction_code);
CREATE INDEX idx_tx_status_created    ON transactions(status, created_at DESC);

-- Người dùng
CREATE INDEX idx_users_email          ON users(email);
CREATE INDEX idx_users_username       ON users(username);

-- Thông báo
CREATE INDEX idx_notif_user_read      ON notifications(user_id, is_read);

-- Lịch sử đăng nhập
CREATE INDEX idx_login_user_date      ON login_history(user_id, logged_at DESC);

-- Thanh toán hóa đơn
CREATE INDEX idx_billpay_customer     ON bill_payments(customer_id, paid_at DESC);

-- Audit log
CREATE INDEX idx_audit_table_target   ON audit_logs(target_table, target_id);
CREATE INDEX idx_audit_user_date      ON audit_logs(user_id, created_at DESC);
```

**Minh chứng tối ưu — So sánh trước/sau index:**

Mục đích: chứng minh index `idx_tx_from_account` giúp query lịch sử giao dịch nhanh hơn đáng kể.

*Bước 1 — Chạy query TRƯỚC khi có index (disable tạm thời để đo):*
```sql
-- Disable index tạm để đo baseline
ALTER INDEX idx_tx_from_account ON transactions DISABLE;
GO

SET STATISTICS IO ON;
SET STATISTICS TIME ON;

-- Query lịch sử giao dịch của một tài khoản
SELECT t.transaction_id, t.transaction_code, t.amount,
       t.transaction_type, t.status, t.created_at, t.description
FROM transactions t
WHERE t.from_account_id = 1
ORDER BY t.created_at DESC;

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
-- Ghi lại: Logical reads = ?, CPU time = ?, Elapsed time = ?
```

*Bước 2 — Bật lại index, chạy lại cùng query:*
```sql
ALTER INDEX idx_tx_from_account ON transactions REBUILD;
GO

SET STATISTICS IO ON;
SET STATISTICS TIME ON;

SELECT t.transaction_id, t.transaction_code, t.amount,
       t.transaction_type, t.status, t.created_at, t.description
FROM transactions t
WHERE t.from_account_id = 1
ORDER BY t.created_at DESC;

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
-- Ghi lại: Logical reads = ?, CPU time = ?, Elapsed time = ?
-- So sánh: logical reads giảm bao nhiêu %
```

*Bước 3 — Query phức tạp hơn: Tổng giao dịch theo tháng (dùng cho báo cáo):*
```sql
SET STATISTICS IO ON;
SET STATISTICS TIME ON;

SELECT
    YEAR(created_at)  AS nam,
    MONTH(created_at) AS thang,
    transaction_type,
    COUNT(*)          AS so_giao_dich,
    SUM(amount)       AS tong_tien
FROM transactions
WHERE status = 'SUCCESS'
  AND created_at >= DATEADD(MONTH, -6, GETDATE())
GROUP BY YEAR(created_at), MONTH(created_at), transaction_type
ORDER BY nam DESC, thang DESC;

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
```

*Bước 4 — Xem Execution Plan trong SSMS:*
- Bật **Include Actual Execution Plan** (Ctrl+M) trước khi chạy query
- Chụp ảnh màn hình Execution Plan để đưa vào tài liệu
- Chú ý các node: **Index Seek** (tốt) vs **Table Scan / Index Scan** (cần xem lại)
- So sánh **Estimated Subtree Cost** trước và sau khi thêm index

**Bảng tổng hợp kết quả đo (điền vào sau khi chạy):**

| Query | Logical Reads (không có index) | Logical Reads (có index) | Cải thiện |
|---|---|---|---|
| Lịch sử GD theo account_id | ??? | ??? | ???% |
| Tổng GD theo tháng | ??? | ??? | ???% |

---

### Task 3.5 — Backup & Recovery

**Chiến lược sao lưu:**

| Loại backup | Tần suất | Mô tả |
|---|---|---|
| Full Backup | Hàng tuần (Chủ nhật 2:00 AM) | Sao lưu toàn bộ database |
| Differential Backup | Hàng ngày (2:00 AM các ngày còn lại) | Chỉ sao lưu phần thay đổi từ Full Backup gần nhất |
| Transaction Log Backup | Mỗi 1 giờ | Cho phép khôi phục đến thời điểm bất kỳ (Point-in-Time Recovery) |

**Scripts:**

```sql
-- 1. Full Backup (chạy hàng tuần)
BACKUP DATABASE banking_db
TO DISK = 'C:\Backup\banking_db_full_' + FORMAT(GETDATE(), 'yyyyMMdd') + '.bak'
WITH COMPRESSION, STATS = 10, CHECKSUM;
GO

-- 2. Differential Backup (chạy hàng ngày)
BACKUP DATABASE banking_db
TO DISK = 'C:\Backup\banking_db_diff_' + FORMAT(GETDATE(), 'yyyyMMdd_HHmm') + '.bak'
WITH DIFFERENTIAL, COMPRESSION, CHECKSUM;
GO

-- 3. Transaction Log Backup (chạy mỗi giờ)
-- Yêu cầu: Recovery Model phải là FULL
ALTER DATABASE banking_db SET RECOVERY FULL;
GO

BACKUP LOG banking_db
TO DISK = 'C:\Backup\banking_db_log_' + FORMAT(GETDATE(), 'yyyyMMdd_HHmm') + '.trn'
WITH COMPRESSION, STATS = 10;
GO
```

**Restore theo từng tình huống:**

```sql
-- TÌNH HUỐNG 1: Restore toàn bộ từ Full Backup
RESTORE DATABASE banking_db
FROM DISK = 'C:\Backup\banking_db_full_20240101.bak'
WITH REPLACE, NORECOVERY;  -- NORECOVERY nếu còn áp thêm log/diff

-- Nếu chỉ có Full Backup, dùng RECOVERY để mở DB:
RESTORE DATABASE banking_db
FROM DISK = 'C:\Backup\banking_db_full_20240101.bak'
WITH REPLACE, RECOVERY;
GO

-- TÌNH HUỐNG 2: Restore Full + Differential
RESTORE DATABASE banking_db
FROM DISK = 'C:\Backup\banking_db_full_20240101.bak'
WITH REPLACE, NORECOVERY;

RESTORE DATABASE banking_db
FROM DISK = 'C:\Backup\banking_db_diff_20240105.bak'
WITH NORECOVERY;

-- Áp log cuối cùng để mở DB
RESTORE LOG banking_db
FROM DISK = 'C:\Backup\banking_db_log_20240105_1400.trn'
WITH RECOVERY;
GO

-- TÌNH HUỐNG 3: Point-in-Time Recovery (khôi phục đến thời điểm cụ thể)
RESTORE DATABASE banking_db
FROM DISK = 'C:\Backup\banking_db_full_20240101.bak'
WITH REPLACE, NORECOVERY;

RESTORE LOG banking_db
FROM DISK = 'C:\Backup\banking_db_log_20240105_1400.trn'
WITH STOPAT = '2024-01-05 13:45:00', RECOVERY;
GO

-- Kiểm tra tính toàn vẹn backup trước khi restore
RESTORE VERIFYONLY
FROM DISK = 'C:\Backup\banking_db_full_20240101.bak';
GO
```

---

### Task 3.6 — Data Dictionary

> Mô tả chi tiết từng bảng và cột theo yêu cầu tài liệu đề bài.

---

#### Bảng: `users`
**Mục đích:** Lưu thông tin đăng nhập và phân quyền của tất cả người dùng hệ thống (khách hàng, nhân viên, admin).

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| user_id | INT | PK, IDENTITY(1,1) | Khóa chính, tự tăng |
| username | NVARCHAR(50) | NOT NULL, UNIQUE | Tên đăng nhập, duy nhất trong hệ thống |
| password | NVARCHAR(255) | NOT NULL | Mật khẩu đã mã hóa BCrypt |
| email | NVARCHAR(100) | NOT NULL, UNIQUE | Email liên hệ, dùng để reset mật khẩu |
| role | NVARCHAR(20) | NOT NULL, CHECK IN ('CUSTOMER','EMPLOYEE','ADMIN') | Vai trò người dùng, xác định quyền truy cập |
| is_active | BIT | NOT NULL, DEFAULT 1 | Trạng thái tài khoản (1=hoạt động, 0=bị khóa) |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm tạo tài khoản |
| updated_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm cập nhật gần nhất |

---

#### Bảng: `customers`
**Mục đích:** Lưu thông tin cá nhân của khách hàng, quan hệ 1-1 với bảng `users`.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| customer_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| user_id | INT | NOT NULL, UNIQUE, FK→users | Liên kết với tài khoản đăng nhập |
| full_name | NVARCHAR(100) | NOT NULL | Họ và tên đầy đủ |
| id_number | NVARCHAR(20) | NOT NULL, UNIQUE | Số CMND/CCCD |
| phone | NVARCHAR(15) | NOT NULL, UNIQUE | Số điện thoại |
| date_of_birth | DATE | NOT NULL | Ngày sinh |
| address | NVARCHAR(255) | NULL | Địa chỉ thường trú |
| avatar_url | NVARCHAR(500) | NULL | Đường dẫn ảnh đại diện |
| kyc_status | NVARCHAR(20) | NOT NULL, CHECK IN ('PENDING','VERIFIED','REJECTED') | Trạng thái xác thực danh tính (eKYC) |

---

#### Bảng: `accounts`
**Mục đích:** Lưu thông tin tài khoản ngân hàng của khách hàng. Một khách hàng có thể có nhiều tài khoản.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| account_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| account_number | NVARCHAR(20) | NOT NULL, UNIQUE | Số tài khoản ngân hàng |
| customer_id | INT | NOT NULL, FK→customers | Chủ tài khoản |
| account_type | NVARCHAR(20) | NOT NULL, CHECK IN ('PAYMENT','SAVINGS') | Loại tài khoản: thanh toán hoặc tiết kiệm |
| balance | DECIMAL(18,2) | NOT NULL, DEFAULT 0, CHECK ≥ 0 | Số dư hiện tại (VND) |
| currency | NCHAR(3) | NOT NULL, DEFAULT 'VND' | Đơn vị tiền tệ |
| status | NVARCHAR(20) | NOT NULL, CHECK IN ('ACTIVE','LOCKED','CLOSED') | Trạng thái tài khoản |
| is_default | BIT | NOT NULL, DEFAULT 0 | Đánh dấu tài khoản mặc định |
| daily_limit | DECIMAL(18,2) | NOT NULL, DEFAULT 100,000,000 | Hạn mức giao dịch tối đa mỗi ngày (VND) |
| opened_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Ngày mở tài khoản |
| closed_at | DATETIME2 | NULL | Ngày đóng tài khoản (nếu có) |
| term_months | INT | NULL | Kỳ hạn tiết kiệm (chỉ dùng cho SAVINGS) |
| interest_rate | DECIMAL(5,2) | NULL | Lãi suất áp dụng (chỉ dùng cho SAVINGS) |
| maturity_date | DATE | NULL | Ngày đáo hạn (chỉ dùng cho SAVINGS) |

---

#### Bảng: `transactions`
**Mục đích:** Ghi lại toàn bộ lịch sử giao dịch tài chính. Đây là bảng trung tâm, có lượng dữ liệu lớn nhất.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| transaction_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| transaction_code | NVARCHAR(30) | NOT NULL, UNIQUE | Mã giao dịch duy nhất để tra cứu |
| from_account_id | INT | NULL, FK→accounts | Tài khoản nguồn (NULL nếu là nạp tiền) |
| to_account_id | INT | NULL, FK→accounts | Tài khoản đích (NULL nếu là rút tiền) |
| amount | DECIMAL(18,2) | NOT NULL | Số tiền giao dịch |
| fee | DECIMAL(18,2) | NOT NULL, DEFAULT 0 | Phí giao dịch |
| transaction_type | NVARCHAR(20) | NOT NULL, CHECK IN ('TRANSFER','DEPOSIT','WITHDRAWAL','FEE','PAYMENT','REVERSAL') | Loại giao dịch |
| status | NVARCHAR(20) | NOT NULL, DEFAULT 'PENDING', CHECK IN ('PENDING','SUCCESS','FAILED','CANCELLED','REVERSED') | Trạng thái xử lý |
| description | NVARCHAR(255) | NULL | Nội dung/ghi chú giao dịch |
| reference_code | NVARCHAR(50) | NULL | Mã tham chiếu liên ngân hàng |
| is_suspicious | BIT | NOT NULL, DEFAULT 0 | Cờ đánh dấu giao dịch đáng ngờ |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm tạo lệnh giao dịch |
| completed_at | DATETIME2 | NULL | Thời điểm hoàn thành giao dịch |

---

#### Bảng: `beneficiaries`
**Mục đích:** Lưu danh sách người thụ hưởng mà khách hàng hay chuyển tiền, giúp tái sử dụng nhanh.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| beneficiary_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| customer_id | INT | NOT NULL, FK→customers | Khách hàng sở hữu danh bạ này |
| account_number | NVARCHAR(20) | NOT NULL | Số tài khoản người thụ hưởng |
| bank_name | NVARCHAR(100) | NOT NULL, DEFAULT 'Nội bộ' | Tên ngân hàng của người thụ hưởng |
| full_name | NVARCHAR(100) | NOT NULL | Tên người thụ hưởng |
| nickname | NVARCHAR(50) | NULL | Tên gợi nhớ do khách đặt |
| is_active | BIT | NOT NULL, DEFAULT 1 | Trạng thái (1=còn dùng, 0=đã xóa mềm) |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Ngày thêm vào danh bạ |

---

#### Bảng: `cards`
**Mục đích:** Quản lý thẻ ngân hàng (thẻ ghi nợ, thẻ tín dụng) liên kết với tài khoản.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| card_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| card_number | NVARCHAR(20) | NOT NULL, UNIQUE | Số thẻ (lưu dạng masked/hash) |
| account_id | INT | NOT NULL, FK→accounts | Tài khoản liên kết |
| card_type | NVARCHAR(20) | NOT NULL, CHECK IN ('DEBIT','CREDIT') | Loại thẻ |
| status | NVARCHAR(20) | NOT NULL, CHECK IN ('ACTIVE','LOCKED','EXPIRED','CANCELLED') | Trạng thái thẻ |
| expiry_date | DATE | NOT NULL | Ngày hết hạn thẻ |
| daily_limit | DECIMAL(18,2) | NOT NULL, DEFAULT 50,000,000 | Hạn mức chi tiêu mỗi ngày |
| allow_international | BIT | NOT NULL, DEFAULT 0 | Cho phép thanh toán quốc tế |
| allow_online | BIT | NOT NULL, DEFAULT 1 | Cho phép thanh toán online |
| issued_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Ngày phát hành thẻ |

---

#### Bảng: `notifications`
**Mục đích:** Lưu trữ tất cả thông báo gửi đến người dùng (biến động số dư, bảo mật, hệ thống...).

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| notification_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| user_id | INT | NOT NULL, FK→users | Người nhận thông báo |
| title | NVARCHAR(200) | NOT NULL | Tiêu đề thông báo |
| content | NVARCHAR(1000) | NOT NULL | Nội dung chi tiết |
| type | NVARCHAR(30) | NOT NULL, CHECK IN ('TRANSACTION','SECURITY','SYSTEM','PROMOTION','REMINDER') | Phân loại thông báo |
| is_read | BIT | NOT NULL, DEFAULT 0 | Trạng thái đã đọc |
| related_tx_id | INT | NULL, FK→transactions | Giao dịch liên quan (nếu có) |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm tạo thông báo |

---

#### Bảng: `bill_payments`
**Mục đích:** Lưu lịch sử thanh toán hóa đơn dịch vụ (điện, nước, internet...) của khách hàng.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| bill_payment_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| customer_id | INT | NOT NULL, FK→customers | Khách hàng thực hiện thanh toán |
| from_account_id | INT | NOT NULL, FK→accounts | Tài khoản bị trừ tiền |
| bill_type | NVARCHAR(30) | NOT NULL, CHECK IN (...) | Loại hóa đơn |
| provider_name | NVARCHAR(100) | NOT NULL | Tên nhà cung cấp dịch vụ |
| customer_code | NVARCHAR(50) | NOT NULL | Mã khách hàng tại nhà cung cấp |
| amount | DECIMAL(18,2) | NOT NULL | Số tiền thanh toán |
| status | NVARCHAR(20) | NOT NULL, DEFAULT 'SUCCESS' | Kết quả thanh toán |
| transaction_id | INT | NULL, FK→transactions | Giao dịch tài chính tương ứng |
| paid_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm thanh toán |

---

#### Bảng: `saved_bills`
**Mục đích:** Lưu hóa đơn thường dùng để thanh toán nhanh lần sau.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| saved_bill_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| customer_id | INT | NOT NULL, FK→customers | Khách hàng lưu hóa đơn |
| bill_type | NVARCHAR(30) | NOT NULL | Loại hóa đơn |
| provider_name | NVARCHAR(100) | NOT NULL | Nhà cung cấp |
| customer_code | NVARCHAR(50) | NOT NULL | Mã khách hàng tại nhà cung cấp |
| nickname | NVARCHAR(100) | NULL | Tên gợi nhớ |
| is_active | BIT | NOT NULL, DEFAULT 1 | Trạng thái (xóa mềm) |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Ngày lưu |

---

#### Bảng: `login_history`
**Mục đích:** Ghi lại mọi lần đăng nhập thành công và thất bại để phục vụ bảo mật và audit.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| login_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| user_id | INT | NOT NULL, FK→users | Người dùng đăng nhập |
| ip_address | NVARCHAR(50) | NULL | Địa chỉ IP thiết bị |
| device_info | NVARCHAR(255) | NULL | Thông tin thiết bị (User-Agent) |
| status | NVARCHAR(20) | NOT NULL, CHECK IN ('SUCCESS','FAILED') | Kết quả đăng nhập |
| failure_reason | NVARCHAR(100) | NULL | Lý do thất bại (sai mật khẩu, bị khóa...) |
| logged_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm đăng nhập |

---

#### Bảng: `audit_logs`
**Mục đích:** Ghi lại mọi thao tác thay đổi dữ liệu quan trọng trong hệ thống, phục vụ kiểm tra và điều tra.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| log_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| user_id | INT | NULL, FK→users | Người thực hiện hành động (NULL nếu là trigger tự động) |
| action | NVARCHAR(100) | NOT NULL | Mô tả hành động (VD: 'UPDATE_ACCOUNT_STATUS') |
| target_table | NVARCHAR(50) | NULL | Bảng bị tác động |
| target_id | INT | NULL | ID của bản ghi bị tác động |
| old_value | NVARCHAR(MAX) | NULL | Snapshot JSON giá trị trước khi thay đổi |
| new_value | NVARCHAR(MAX) | NULL | Snapshot JSON giá trị sau khi thay đổi |
| ip_address | NVARCHAR(50) | NULL | Địa chỉ IP của người thực hiện |
| created_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Thời điểm ghi log |

---

#### Bảng: `interest_rates`
**Mục đích:** Lưu bảng lãi suất tiết kiệm theo kỳ hạn, có hiệu lực theo thời gian.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| rate_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| term_months | INT | NOT NULL | Kỳ hạn (tháng) |
| rate_percent | DECIMAL(5,2) | NOT NULL | Lãi suất (%/năm) |
| effective_from | DATE | NOT NULL | Ngày bắt đầu áp dụng |
| effective_to | DATE | NULL | Ngày kết thúc áp dụng (NULL = đang hiệu lực) |
| is_active | BIT | NOT NULL, DEFAULT 1 | Trạng thái hiệu lực |

---

#### Bảng: `system_config`
**Mục đích:** Lưu các tham số cấu hình hệ thống có thể thay đổi mà không cần deploy lại (hạn mức, timeout...).

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mục đích |
|---|---|---|---|
| config_id | INT | PK, IDENTITY(1,1) | Khóa chính |
| config_key | NVARCHAR(100) | NOT NULL, UNIQUE | Tên tham số (VD: 'MAX_DAILY_TRANSFER') |
| config_value | NVARCHAR(500) | NOT NULL | Giá trị tham số |
| description | NVARCHAR(255) | NULL | Mô tả ý nghĩa tham số |
| updated_at | DATETIME2 | NOT NULL, DEFAULT GETDATE() | Lần cập nhật gần nhất |

---

### Task 3.7 — Triggers

> Triggers đảm bảo tính toàn vẹn dữ liệu và tự động hóa các tác vụ nghiệp vụ trực tiếp ở tầng database, không phụ thuộc vào backend.

---

**Trigger 1 — Tự động ghi `audit_logs` khi thay đổi trạng thái tài khoản:**

```sql
CREATE TRIGGER trg_accounts_audit
ON accounts
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Chỉ ghi log khi trạng thái (status) hoặc số dư (balance) thay đổi
    IF UPDATE(status) OR UPDATE(balance)
    BEGIN
        INSERT INTO audit_logs (action, target_table, target_id, old_value, new_value)
        SELECT
            CASE
                WHEN d.status <> i.status THEN 'UPDATE_ACCOUNT_STATUS'
                WHEN d.balance <> i.balance THEN 'UPDATE_ACCOUNT_BALANCE'
                ELSE 'UPDATE_ACCOUNT'
            END,
            'accounts',
            i.account_id,
            -- Snapshot JSON giá trị cũ
            (SELECT d.account_id, d.account_number, d.status, d.balance, d.daily_limit
             FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            -- Snapshot JSON giá trị mới
            (SELECT i.account_id, i.account_number, i.status, i.balance, i.daily_limit
             FOR JSON PATH, WITHOUT_ARRAY_WRAPPER)
        FROM inserted i
        JOIN deleted d ON i.account_id = d.account_id
        WHERE d.status <> i.status OR d.balance <> i.balance;
    END
END;
GO
```

---

**Trigger 2 — Tự động tạo thông báo khi có giao dịch thành công:**

```sql
CREATE TRIGGER trg_transactions_notify
ON transactions
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @tx_id      INT;
    DECLARE @amount     DECIMAL(18,2);
    DECLARE @tx_type    NVARCHAR(20);
    DECLARE @from_id    INT;
    DECLARE @to_id      INT;

    SELECT @tx_id   = transaction_id,
           @amount  = amount,
           @tx_type = transaction_type,
           @from_id = from_account_id,
           @to_id   = to_account_id
    FROM inserted
    WHERE status = 'SUCCESS';

    IF @tx_id IS NULL RETURN;  -- Không tạo thông báo cho giao dịch failed/pending

    -- Thông báo cho tài khoản nguồn (tiền đi ra)
    IF @from_id IS NOT NULL
    BEGIN
        INSERT INTO notifications (user_id, title, content, type, related_tx_id)
        SELECT
            c.user_id,
            N'Biến động số dư',
            N'Tài khoản của bạn vừa bị trừ ' + FORMAT(@amount, 'N0') + N' VND. Mã GD: ' +
            (SELECT transaction_code FROM transactions WHERE transaction_id = @tx_id),
            'TRANSACTION',
            @tx_id
        FROM accounts a
        JOIN customers c ON a.customer_id = c.customer_id
        WHERE a.account_id = @from_id;
    END

    -- Thông báo cho tài khoản đích (tiền đi vào)
    IF @to_id IS NOT NULL AND @tx_type IN ('TRANSFER', 'DEPOSIT')
    BEGIN
        INSERT INTO notifications (user_id, title, content, type, related_tx_id)
        SELECT
            c.user_id,
            N'Biến động số dư',
            N'Tài khoản của bạn vừa được cộng ' + FORMAT(@amount, 'N0') + N' VND. Mã GD: ' +
            (SELECT transaction_code FROM transactions WHERE transaction_id = @tx_id),
            'TRANSACTION',
            @tx_id
        FROM accounts a
        JOIN customers c ON a.customer_id = c.customer_id
        WHERE a.account_id = @to_id;
    END
END;
GO
```

---

**Trigger 3 — Tự động ghi `audit_logs` khi thay đổi role hoặc trạng thái người dùng:**

```sql
CREATE TRIGGER trg_users_audit
ON users
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(role) OR UPDATE(is_active)
    BEGIN
        INSERT INTO audit_logs (action, target_table, target_id, old_value, new_value)
        SELECT
            CASE
                WHEN d.role <> i.role THEN 'UPDATE_USER_ROLE'
                WHEN d.is_active <> i.is_active THEN 'UPDATE_USER_STATUS'
                ELSE 'UPDATE_USER'
            END,
            'users',
            i.user_id,
            (SELECT d.user_id, d.username, d.role, d.is_active FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            (SELECT i.user_id, i.username, i.role, i.is_active FOR JSON PATH, WITHOUT_ARRAY_WRAPPER)
        FROM inserted i
        JOIN deleted d ON i.user_id = d.user_id
        WHERE d.role <> i.role OR d.is_active <> i.is_active;
    END
END;
GO
```

---

**Trigger 4 — Ngăn xóa tài khoản còn số dư:**

```sql
CREATE TRIGGER trg_accounts_prevent_delete
ON accounts
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (SELECT 1 FROM deleted WHERE balance > 0)
    BEGIN
        RAISERROR(N'Không thể xóa tài khoản còn số dư. Vui lòng rút hết tiền trước.', 16, 1);
        RETURN;
    END

    -- Cho phép xóa nếu số dư = 0
    DELETE FROM accounts WHERE account_id IN (SELECT account_id FROM deleted);
END;
GO
```

---

### Task 3.8 — Views

> Views đóng gói các query phức tạp, tái sử dụng cho cả backend và báo cáo, đồng thời ẩn đi logic join nhiều bảng.

---

**View 1 — Tổng quan tài khoản khách hàng:**

```sql
CREATE VIEW vw_customer_account_summary AS
SELECT
    c.customer_id,
    c.full_name,
    c.phone,
    u.email,
    c.kyc_status,
    COUNT(a.account_id)                         AS so_tai_khoan,
    SUM(CASE WHEN a.status = 'ACTIVE'
             THEN a.balance ELSE 0 END)         AS tong_so_du,
    SUM(CASE WHEN a.account_type = 'SAVINGS'
             AND a.status = 'ACTIVE'
             THEN a.balance ELSE 0 END)         AS so_du_tiet_kiem,
    SUM(CASE WHEN a.account_type = 'PAYMENT'
             AND a.status = 'ACTIVE'
             THEN a.balance ELSE 0 END)         AS so_du_thanh_toan,
    MAX(a.opened_at)                            AS mo_tai_khoan_gan_nhat
FROM customers c
JOIN users u ON c.user_id = u.user_id
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.full_name, c.phone, u.email, c.kyc_status;
GO
```

**Cách dùng:**
```sql
-- Xem tổng quan của khách hàng ID = 1
SELECT * FROM vw_customer_account_summary WHERE customer_id = 1;

-- Top 10 khách hàng có số dư cao nhất
SELECT TOP 10 * FROM vw_customer_account_summary ORDER BY tong_so_du DESC;
```

---

**View 2 — Báo cáo giao dịch theo tháng:**

```sql
CREATE VIEW vw_monthly_transaction_report AS
SELECT
    YEAR(created_at)                AS nam,
    MONTH(created_at)               AS thang,
    transaction_type,
    COUNT(*)                        AS so_giao_dich,
    SUM(amount)                     AS tong_tien,
    AVG(amount)                     AS trung_binh,
    MAX(amount)                     AS gia_tri_cao_nhat,
    MIN(amount)                     AS gia_tri_thap_nhat,
    SUM(CASE WHEN status = 'SUCCESS'  THEN 1 ELSE 0 END) AS thanh_cong,
    SUM(CASE WHEN status = 'FAILED'   THEN 1 ELSE 0 END) AS that_bai,
    SUM(fee)                        AS tong_phi
FROM transactions
GROUP BY YEAR(created_at), MONTH(created_at), transaction_type;
GO
```

**Cách dùng:**
```sql
-- Báo cáo 6 tháng gần nhất
SELECT * FROM vw_monthly_transaction_report
WHERE nam = YEAR(GETDATE())
ORDER BY nam DESC, thang DESC;
```

---

**View 3 — Chi tiết giao dịch kèm thông tin tài khoản:**

```sql
CREATE VIEW vw_transaction_detail AS
SELECT
    t.transaction_id,
    t.transaction_code,
    t.transaction_type,
    t.amount,
    t.fee,
    t.status,
    t.description,
    t.is_suspicious,
    t.created_at,
    t.completed_at,
    -- Tài khoản nguồn
    fa.account_number   AS from_account_number,
    fc.full_name        AS from_customer_name,
    -- Tài khoản đích
    ta.account_number   AS to_account_number,
    tc.full_name        AS to_customer_name
FROM transactions t
LEFT JOIN accounts fa  ON t.from_account_id = fa.account_id
LEFT JOIN customers fc ON fa.customer_id = fc.customer_id
LEFT JOIN accounts ta  ON t.to_account_id = ta.account_id
LEFT JOIN customers tc ON ta.customer_id = tc.customer_id;
GO
```

**Cách dùng:**
```sql
-- Tra cứu giao dịch theo mã
SELECT * FROM vw_transaction_detail WHERE transaction_code = 'TXN20240105123456ABC123';

-- Lịch sử giao dịch của một số tài khoản
SELECT * FROM vw_transaction_detail
WHERE from_account_number = '1000000001' OR to_account_number = '1000000001'
ORDER BY created_at DESC;
```

---

**View 4 — Dashboard Admin (thống kê hôm nay):**

```sql
CREATE VIEW vw_admin_dashboard_today AS
SELECT
    (SELECT COUNT(*) FROM users WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE))
        AS khach_hang_moi_hom_nay,
    (SELECT COUNT(*) FROM transactions WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE))
        AS tong_giao_dich_hom_nay,
    (SELECT ISNULL(SUM(amount), 0) FROM transactions
     WHERE status = 'SUCCESS' AND CAST(created_at AS DATE) = CAST(GETDATE() AS DATE))
        AS tong_tien_giao_dich_hom_nay,
    (SELECT COUNT(*) FROM transactions
     WHERE status = 'FAILED' AND CAST(created_at AS DATE) = CAST(GETDATE() AS DATE))
        AS giao_dich_that_bai_hom_nay,
    (SELECT COUNT(*) FROM accounts WHERE status = 'LOCKED')
        AS tai_khoan_dang_khoa,
    (SELECT ISNULL(SUM(balance), 0) FROM accounts WHERE status = 'ACTIVE')
        AS tong_so_du_toan_he_thong;
GO
```

**Cách dùng:**
```sql
SELECT * FROM vw_admin_dashboard_today;
```

---

## GIAI ĐOẠN 4 — Backend (Java Spring Boot)

### Task 4.1 — Cấu hình Security + JWT

**Cách làm:**
1. Thêm dependency `jjwt` vào `pom.xml`
2. Tạo class `JwtUtil` để generate/validate token
3. Tạo `JwtAuthenticationFilter` extends `OncePerRequestFilter`
4. Cấu hình `SecurityFilterChain` trong `SecurityConfig`:
   - Permit: `/api/auth/**`
   - Authenticated: tất cả route còn lại
5. Implement `UserDetailsService` để load user từ DB

**Flow xác thực:**
```
Client → POST /api/auth/login (username, password)
       → Spring Security xác thực → UserDetailsService → DB
       → Trả về JWT token
       → Client gửi kèm header: Authorization: Bearer <token>
       → Filter validate token → Set SecurityContext
```

---

### Task 4.2 — Implement CRUD và Business Logic

**Với mỗi tính năng, tạo đủ 4 lớp: Entity → Repository → Service → Controller.**

**Ví dụ flow Chuyển tiền:**
```
POST /api/transactions/transfer
  → TransactionController.transfer(TransferRequest dto)
  → TransactionService.transfer(dto)
      → Validate số tiền, tài khoản
      → Gọi stored procedure sp_transfer_money qua JPA / EntityManager
      → Trả về TransactionResponse
  → Controller trả về ResponseEntity<ApiResponse>
```

**Xử lý lỗi tập trung:**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse> handleInsufficientBalance(InsufficientBalanceException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
}
```

---

### Task 4.3 — Viết API Documentation (Swagger)

**Cách làm:**
1. Thêm `springdoc-openapi-starter-webmvc-ui` vào `pom.xml`
2. Truy cập `http://localhost:8080/swagger-ui.html` để xem docs
3. Annotate controller với `@Operation`, `@ApiResponse`

---

## GIAI ĐOẠN 5 — Frontend Web (React + Ant Design)

### Task 5.1 — Setup Routing và Layout

```jsx
<BrowserRouter>
  <Routes>
    <Route path="/login"     element={<LoginPage />} />
    <Route path="/register"  element={<RegisterPage />} />
    <Route element={<PrivateRoute />}>
      <Route path="/" element={<MainLayout />}>
        <Route path="dashboard"      element={<DashboardPage />} />
        <Route path="transfer"       element={<TransferPage />} />
        <Route path="transactions"   element={<TransactionHistoryPage />} />
        <Route path="beneficiaries"  element={<BeneficiariesPage />} />
        <Route path="bills"          element={<BillPaymentPage />} />
        <Route path="cards"          element={<CardsPage />} />
        <Route path="notifications"  element={<NotificationsPage />} />
        <Route path="profile"        element={<ProfilePage />} />
      </Route>
      <Route path="/admin" element={<AdminLayout />}>
        <Route path="dashboard"    element={<AdminDashboardPage />} />
        <Route path="customers"    element={<CustomerManagementPage />} />
        <Route path="accounts"     element={<AccountManagementPage />} />
        <Route path="transactions" element={<TransactionManagementPage />} />
        <Route path="audit-logs"   element={<AuditLogPage />} />
      </Route>
    </Route>
  </Routes>
</BrowserRouter>
```

**Layout chính:** Ant Design `Layout` với `Sider` + `Header` + `Content`.

---

### Task 5.2 — Cấu hình Axios và API Layer

```javascript
// src/api/axiosInstance.js
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) window.location.href = '/login';
    return Promise.reject(err);
  }
);
```

---

### Task 5.3 — Xây dựng các trang chính

| Trang | Component Ant Design chính |
|---|---|
| Login | `Form`, `Input`, `Button` |
| Dashboard | `Card`, `Statistic`, `Table` |
| Chuyển tiền | `Form`, `Steps`, `Modal xác nhận` |
| Lịch sử giao dịch | `Table`, `DatePicker`, `Tag` |
| Danh bạ người thụ hưởng | `List`, `Modal`, `Form` |
| Thanh toán hóa đơn | `Form`, `Select`, `Steps` |
| Quản lý thẻ | `Card`, `Switch`, `Modal` |
| Thông báo | `List`, `Badge`, `Tag` |
| Hồ sơ cá nhân | `Descriptions`, `Form` |
| Admin Dashboard | `Statistic`, `Chart`, `Table` |
| Admin - Quản lý KH | `Table`, `Drawer`, `Button` |
| Admin - Giao dịch | `Table`, `Tag`, `Modal` |
| Admin - Audit Log | `Table`, `Timeline` |

---

### Task 5.4 — Quản lý State (Zustand)

```javascript
// src/store/authStore.js
const useAuthStore = create((set) => ({
  user: null,
  token: localStorage.getItem('token'),
  login: (userData, token) => {
    localStorage.setItem('token', token);
    set({ user: userData, token });
  },
  logout: () => {
    localStorage.removeItem('token');
    set({ user: null, token: null });
  }
}));
```

---

## GIAI ĐOẠN 6 — Kiểm Thử & Hoàn Thiện Web App

### Task 6.1 — Kiểm thử API với Postman

1. Tạo Postman Collection: `Banking App API`
2. Tạo Environment với biến `base_url`, `token`
3. Viết test script tự động gán token sau login
4. Test theo thứ tự: Auth → Account → Transaction → Bill Payment → Admin

---

### Task 6.2 — Kiểm thử nghiệp vụ

| Kịch bản | Kết quả mong đợi |
|---|---|
| Chuyển tiền đủ số dư | Thành công, cập nhật số dư 2 bên |
| Chuyển tiền thiếu số dư | Lỗi "Số dư không đủ" |
| Chuyển sang TK không tồn tại | Lỗi "Tài khoản không tồn tại" |
| Chuyển tiền vào chính mình | Lỗi "Không thể chuyển vào chính tài khoản này" |
| Chuyển tiền vượt hạn mức ngày | Lỗi "Vượt hạn mức giao dịch trong ngày" |
| Nạp tiền hợp lệ | Số dư tăng, ghi transaction, nhận notification |
| Rút tiền vượt số dư | Lỗi "Số dư không đủ" |
| Thanh toán hóa đơn | Số dư giảm, ghi bill_payment + transaction |
| Đăng nhập sai mật khẩu | Lỗi 401 |
| Truy cập API không có token | Lỗi 401 |
| Admin khóa tài khoản KH | Tài khoản chuyển sang LOCKED, audit_log ghi nhận |
| Xóa tài khoản còn số dư | Trigger chặn, báo lỗi |

---

### Task 6.3 — Checklist Tài liệu Báo Cáo

- [ ] Mô tả chức năng hệ thống (dựa theo Task 1.1)
- [ ] ERD (export từ SSMS)
- [ ] Data Dictionary (Task 3.6 — đã có)
- [ ] Migration scripts V001 → V013 UP + DOWN có mô tả
- [ ] Seed script có mô tả
- [ ] Danh sách Index + mục đích
- [ ] Triggers: mã nguồn + mục đích từng trigger
- [ ] Views: mã nguồn + ví dụ sử dụng
- [ ] EXPLAIN query — bảng so sánh Logical Reads trước/sau index
- [ ] Ảnh chụp Execution Plan trong SSMS (Index Seek vs Table Scan)
- [ ] Chiến lược backup (bảng Full/Diff/Log) + script backup/restore
- [ ] Ảnh màn hình ứng dụng web

---

## GIAI ĐOẠN 7 — Mobile App (Android Studio)

> **Bắt đầu sau khi Web App hoàn thiện.** Tái sử dụng toàn bộ Backend API đã có.

### Task 7.1 — Khởi tạo Android Project

1. Mở Android Studio → New Project → Empty Activity
2. Language: Java, Min SDK: API 26 (Android 8)
3. Thêm dependencies vào `build.gradle`:

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    implementation 'androidx.navigation:navigation-fragment:2.7.0'
    implementation 'com.google.android.material:material:1.11.0'
}
```

---

### Task 7.2 — Cấu hình Retrofit + API Layer

```java
public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    // 10.0.2.2 = localhost trong Android emulator

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    String token = TokenManager.getToken();
                    Request req = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                    return chain.proceed(req);
                }).build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
}
```

---

### Task 7.3 — Xây dựng màn hình chính

| Màn hình | Layout chính |
|---|---|
| Đăng nhập | `TextInputLayout`, `MaterialButton` |
| Tổng quan tài khoản | `CardView`, `RecyclerView` |
| Chuyển tiền | `TextInputLayout`, `MaterialButton` |
| Lịch sử giao dịch | `RecyclerView` + `CardView` |
| Thanh toán hóa đơn | `TextInputLayout`, `Spinner` |
| Thông báo | `RecyclerView` + `CardView` |
| Hồ sơ cá nhân | `TextView`, `MaterialButton` |

**Kiến trúc:** MVVM pattern — ViewModel + LiveData. Gọi Retrofit từ Repository class.

---

## Gợi Ý Timeline

| Tuần | Nội dung |
|---|---|
| Tuần 1 | Giai đoạn 1: Phân tích chức năng, thiết kế DB schema |
| Tuần 2 | Giai đoạn 2 + 3: Setup môi trường + Viết toàn bộ T-SQL (V001→V013 UP/DOWN, SP, Triggers, Views, seed, index, backup) |
| Tuần 3 | Giai đoạn 4: Backend Spring Boot (Security, JWT, các API chính) |
| Tuần 4 | Giai đoạn 5: Frontend React (routing, layout, các trang chính) |
| Tuần 5 | Giai đoạn 6: Test, fix bug, hoàn thiện tài liệu Web App |
| Tuần 6–7 | Giai đoạn 7: Android App (Retrofit, màn hình, MVVM) |

---

*Kế hoạch này có thể điều chỉnh tùy theo tiến độ thực tế. Ưu tiên hoàn thiện Database và Backend trước khi làm Frontend.*
