const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.json());

let employees = [
    { id: "1", name: "Trần Minh Huy", position: "Trưởng phòng CNTT", salary: 25000000 },
    { id: "2", name: "Trần Minh Khoa", position: "Lập trình viên Mobile", salary: 18000000 },
    { id: "3", name: "Phạm Thanh Tùng", position: "Lập trình viên Backend", salary: 19500000 },
    { id: "4", name: "Lê Gia Huy", position: "Kỹ sư Hệ thống Embedded", salary: 20000000 },
    { id: "5", name: "Hoàng Đức Nguyên", position: "Kỹ sư Điều khiển Tự động", salary: 17000000 },
    { id: "6", name: "Đỗ Thủy Tiên", position: "Chuyên viên Tuyển dụng IT", salary: 14000000 },
    { id: "7", name: "Nguyễn Văn Khôi", position: "Quản trị viên Hệ thống Mạng", salary: 21500000 },
    { id: "8", name: "Vũ Hoàng Vũ", position: "Chuyên gia Bảo mật Hệ thống", salary: 28000000 },
    { id: "9", name: "Bùi Tiến Đông", position: "Kỹ sư Giao thức Mạng SCADA", salary: 22000000 },
    { id: "10", name: "Nguyễn Minh Châu", position: "Lập trình viên PLC & IoT", salary: 19000000 },
    { id: "11", name: "Trần Đức Hạnh", position: "Phân tích dữ liệu Nhân sự", salary: 16500000 },
    { id: "12", name: "Lê Tuấn Anh", position: "Lập trình viên Frontend", salary: 16000000 },
    { id: "13", name: "Phạm Minh Tú", position: "Kỹ sư DevOps mạng", salary: 24000000 },
    { id: "14", name: "Hoàng Quốc Bảo", position: "UI/UX Designer", salary: 15500000 },
    { id: "15", name: "Đặng Thùy Dương", position: "Chuyên viên Tính lương C&B", salary: 15000000 },
    { id: "16", name: "Ngô Chí Thành", position: "Kiến trúc sư Giải pháp Cloud", salary: 32000000 },
    { id: "17", name: "Vũ Phương Thảo", position: "Quản lý Dự án (Project Manager)", salary: 30000000 }
];
app.use((req, res, next) => {
    res.setHeader('Content-Type', 'application/json; charset=utf-8');
    
    if (req.method === 'GET') {
        res.setHeader('Cache-Control', 'public, max-age=30');
    } else {
        res.setHeader('Cache-Control', 'no-store'); // Không cache với POST, PUT, DELETE dữ liệu thay đổi
    }
    next();
});

app.get('/api/v1/employees', (req, res) => {
    res.status(200).json({
        success: true,
        message: "Lấy danh sách thành công",
        data: employees
    });
});

app.post('/api/v1/employees', (req, res) => {
    const { name, position, salary } = req.body;
    
    if (!name || !position) {
        return res.status(400).json({ success: false, message: "Dữ liệu gửi lên thiếu thông tin!" });
    }

    const newEmployee = {
        id: String(Date.now()), // Tạo ID duy nhất bằng timestamp hệ thống
        name,
        position,
        salary: Number(salary) || 0
    };

    employees.push(newEmployee);
    res.status(201).json({ // Trả về HTTP Code 201 Created
        success: true,
        message: "Đã thêm nhân viên thành công",
        data: newEmployee
    });
});

app.put('/api/v1/employees/:id', (req, res) => {
    const { id } = req.params;
    const { name, position, salary } = req.body;

    let employee = employees.find(emp => emp.id === id);

    if (!employee) {
        return res.status(404).json({ success: false, message: "Không tìm thấy nhân viên trên mạng!" });
    }

    if (name) employee.name = name;
    if (position) employee.position = position;
    if (salary) employee.salary = Number(salary);

    res.status(200).json({
        success: true,
        message: `Đã cập nhật nhân viên ID: ${id}`,
        data: employee
    });
});

app.delete('/api/v1/employees/:id', (req, res) => {
    const { id } = req.params;
    const currentLength = employees.length;
    
    employees = employees.filter(emp => emp.id !== id);

    if (employees.length === currentLength) {
        return res.status(404).json({ success: false, message: "Nhân viên không tồn tại để xóa!" });
    }

    res.status(200).json({
        success: true,
        message: `Xóa thành công nhân viên ID: ${id}`
    });
});

app.listen(PORT, () => {
    console.log(`Server Node.js đang chạy ổn định tại: http://localhost:${PORT}`);
});