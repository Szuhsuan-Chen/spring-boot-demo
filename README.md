# Spring Boot + MySQL 指令速查

## 1) 進入專案
```bash
cd /Users/szuhsuanchen/Desktop/SzuHsuan/20260305_java-spring-boot
```

## 2) 設定資料庫帳密（本次終端機有效）
```bash
export DB_USERNAME=你的資料庫帳號
export DB_PASSWORD=你的資料庫密碼
```

如果資料庫位置不是預設 `localhost:3306/mywebsite`，再加這行：
```bash
export DB_URL=jdbc:mysql://你的主機:3306/你的資料庫名稱
```

## 3) 啟動專案
```bash
mvn spring-boot:run
```

## 4) 驗證環境變數有沒有設成功
```bash
echo $DB_USERNAME
echo $DB_PASSWORD
echo $DB_URL
```

## 5) 結束後清掉敏感變數（可選）
```bash
unset DB_USERNAME
unset DB_PASSWORD
unset DB_URL
```

## 6) 想要每次開終端機都自動帶入（可選）
把下面三行加到 `~/.zshrc`：
```bash
export DB_USERNAME=你的資料庫帳號
export DB_PASSWORD=你的資料庫密碼
export DB_URL=jdbc:mysql://localhost:3306/mywebsite
```
加完後執行：
```bash
source ~/.zshrc
```

