DROP DATABASE IF EXISTS `SportShop_360`;
CREATE DATABASE IF NOT EXISTS `SportShop_360`;
SHOW DATABASES ;
USE `SportShop_360`;

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users(
    UserId VARCHAR(7) NOT NULL,
    UserName VARCHAR(32),
    Address VARCHAR(32),
    Email VARCHAR(32) NOT NULL,
    AccountType VARCHAR(20) NOT NULL DEFAULT 'Cashier',
    Password VARCHAR(24) NOT NULL,
    CONSTRAINT PRIMARY KEY (UserId)
    );
SHOW TABLES ;
DESCRIBE Users;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    CustomerId VARCHAR(7) NOT NULL,
    CustomerName VARCHAR(32),
    Email VARCHAR(32) NOT NULL,
    PhoneNumber INT(10),
    CONSTRAINT PRIMARY KEY (CustomerId)
    );
SHOW TABLES ;
DESCRIBE Customer;

DROP TABLE IF EXISTS Agent;
CREATE TABLE IF NOT EXISTS Agent(
    AgentId VARCHAR(7) NOT NULL,
    AgentName VARCHAR(32) DEFAULT 'Unknown',
    PhoneNumber INT(10),
    Email VARCHAR(32) NOT NULL,
    CompanyName VARCHAR(32) NOT NULL,
    CONSTRAINT PRIMARY KEY (AgentId)
    );
SHOW TABLES ;
DESCRIBE Agent;

DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item(
    ItemCode VARCHAR(7) NOT NULL,
    AgentId VARCHAR(7) NOT NULL,
    Description VARCHAR(64),
    CategoryType VARCHAR(24) DEFAULT 'Not Selected',
    WarrantyPeriod INT(3),
    QtyOnHand INT(3),
    UnitSuppliedPrice DOUBLE,
    UnitPrice DOUBLE,
    UnitProfit DOUBLE,

    CONSTRAINT PRIMARY KEY (ItemCode),
    CONSTRAINT FOREIGN KEY (AgentId) REFERENCES Agent(AgentId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Item;

DROP TABLE IF EXISTS ReservationNote;
CREATE TABLE IF NOT EXISTS ReservationNote(
    ReservationNo VARCHAR(7) NOT NULL,
    CustomerId VARCHAR(7) NOT NULL,
    ReservationDate DATE,
    Contexts VARCHAR(100),

    CONSTRAINT PRIMARY KEY (ReservationNo),
    CONSTRAINT FOREIGN KEY (CustomerId) REFERENCES Customer(CustomerId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE ReservationNote;

DROP TABLE IF EXISTS RepairOrders;
CREATE TABLE IF NOT EXISTS RepairOrders(
    BillNumber VARCHAR(7) NOT NULL,
    CustomerId VARCHAR(7) NOT NULL,
    TotalBillAmount DOUBLE,

    CONSTRAINT PRIMARY KEY (BillNumber),
    CONSTRAINT FOREIGN KEY (CustomerId) REFERENCES Customer(CustomerId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE RepairOrders;

DROP TABLE IF EXISTS RepairItem;
CREATE TABLE IF NOT EXISTS RepairItem(
    Repair_ItemCode VARCHAR(7) NOT NULL,
    BillNumber VARCHAR(7) NOT NULL,
    OrderId VARCHAR(7),
    Description VARCHAR(100),
    RecievedDate DATE NOT NULL,
    ReturnDate DATE,
    OrderItem_Cost DOUBLE,
    externalCharge DOUBLE,

    CONSTRAINT PRIMARY KEY (Repair_ItemCode),
    CONSTRAINT FOREIGN KEY (BillNumber) REFERENCES RepairOrders(BillNumber) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE RepairItem;

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    OrderId VARCHAR(7) NOT NULL,
    UserId VARCHAR(7) NOT NULL,
    CustomerId VARCHAR(7) NOT NULL,
    OrderDate DATE,
    TotalAmount DOUBLE,
    FullDiscount DOUBLE,
    AbsoluteTotal DOUBLE,
    OrderProfit DOUBLE,

    CONSTRAINT PRIMARY KEY (OrderId),
    CONSTRAINT FOREIGN KEY (UserId) REFERENCES Users(UserId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (CustomerId) REFERENCES Customer(CustomerId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE Orders;

DROP TABLE IF EXISTS OrderDetail;
CREATE TABLE IF NOT EXISTS OrderDetail(
    OrderId VARCHAR(7) NOT NULL,
    ItemCode VARCHAR(7) NOT NULL,
    OrderQty INT(3),
    ItemTotal DOUBLE,
    ItemProfit DOUBLE,

    CONSTRAINT PRIMARY KEY (OrderId,ItemCode),
    CONSTRAINT FOREIGN KEY (OrderId) REFERENCES Orders(OrderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (ItemCode) REFERENCES Item(ItemCode) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE OrderDetail;

DROP TABLE IF EXISTS WarrantedRepair_Item;
CREATE TABLE IF NOT EXISTS WarrantedRepair_Item(
    BillNumber VARCHAR(7) NOT NULL,
    OrderId VARCHAR(7) NOT NULL,
    ItemCode VARCHAR(7) NOT NULL,
    ExternalItem_OrderId VARCHAR(7) DEFAULT 'No',
    RepairNote VARCHAR(100),
    ItemRecieved_Date DATE NOT NULL,
    ItemReturn_Date DATE,
    ExternalCost DOUBLE DEFAULT 0.00,

    CONSTRAINT PRIMARY KEY (BillNumber),
    CONSTRAINT FOREIGN KEY (OrderId) REFERENCES Orders(OrderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (ItemCode) REFERENCES Item(ItemCode) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES ;
DESCRIBE WarrantedRepair_Item;

---------------------------------------------------------------------------------------------------------

INSERT INTO Customer VALUES("C-00001","Janith Sandaru","Janith@gmail.com",0754448903);
INSERT INTO Customer VALUES("C-00002","Hemali Lakshika","Lakshi@gmail.com",0714342908);
INSERT INTO Customer VALUES("C-00003","Tharindu Shantha","Tharindu832@gmail.com",0786354273);

SELECT CustomerId FROM Customer WHERE CustomerId=(SELECT max(CustomerId) FROM Customer);

-----------------------------------------------------------------------------------------------------------
INSERT INTO Agent VALUES('A-00001','B Rajapaksha',0763542753,'Banu@gmail.com','Adidas'),
('A-00002','C Asalanka',0763764351,'Asalanka@gmail.com','Adidas'),
('A-00003','A Frenando',0769876234,'Frenando@gmail.com','Puma'),
('A-00004','D Shanaka',0767453982,'Shanaka@gmail.com','Nike'),
('A-00005','C Karunaratne',0763542798,'Karunaratne@gmail.com','Nike'),
('A-00006','M Jayavardane',0767453982,'Mahela2@gmail.com','Kookaburra'),
('A-00007','N Karunaratne',0763542348,'NKarunaratne29@gmail.com','Yonex');

INSERT INTO Item VALUES('I-00001','A-00006','2021 KOOKABURRA GHOST PRO CRICKET BAT','Cricket',36,10,32000.00,34600.00,2600.00),
('I-00002','A-00006','2021 KOOKABURRA PACE PRO CRICKET BAT','Cricket',24,10,27000.00,30500.00,3500.00),
('I-00003','A-00003','PUMA EVOSPEED 4 CRICKET BAT','Cricket',24,5,16000.00,17500.00,1500.00),
('I-00004','A-00007','YONEX MUSCLE POWER 2 RACKET - BLUE','Badminton',12,6,2100.00,2800.00,800.00),
('I-00005','A-00004','Nike E200 T-Shirt','All',0,30,1200.00,1400.00,200.00),
('I-00006','A-00004','Nike E300 Shorts','All',0,30,1400.00,1700.00,300.00),
('I-00007','A-00005','Nike AIR MAX Plus','All',12,15,6500.00,8100.00,1600.00),
('I-00008','A-00001','Adidas Carrom Board XVII','Carrom',24,20,6200.00,8000.00,1800.00),
('I-00009','A-00006','KOOKABURRA Grip Tight V','Cricket',6,40,3100.00,4200.00,1100.00);

WHERE CustomerName LIKE '%or%';

SELECT * FROM Item WHERE Description LIKE '%BAT%';

SELECT * FROM Item WHERE CategoryType='Cricket';

INSERT INTO Users VALUES('U-00001','Janith','Colombo','Janith12@gmail.com','Admin','2001'),
('U-00002','Lakshi','Horana','Lakshi@gmail.com','Admin','1001'),
('U-00003','Udara','Maharagama','Udara@gmail.com','Cashier','3001'),
('U-00004','Kalum','Piliyandala','Kalum@gmail.com','Cashier','4001'),
('U-00005','Sunil','Panadura','Sunil@gmail.com','Cashier','5001');

INSERT INTO Customer VALUES('C-00001','Senani','Senani@gmail.com',0757826379),
('C-00002','Kamala','Kamala@gmail.com',0757812121),
('C-00003','Sumana','Sumana@gmail.com',0786372536),
('C-00004','Ajith','Ajith@gmail.com',0116735473),
('C-00005','Ranil','Ranil@gmail.com',0786735163),
('C-00006','Sajith','Sajith@gmail.com',0727897365),
('C-00007','Saman','Saman@gmail.com',0785630091),
('C-00008','Rohana','Rohana@gmail.com',0758973651),
('C-00009','Upul','Upul@gmail.com',0763514351);


INSERT INTO ORDERS VALUES('O-00001','U-00003','C-00001',2021-09-07);

--------------------------------------------------------------------------

    SELECT repairItem.Repair_ItemCode, repairItem.BillNumber, repairItem.Description, repairItem.RecievedDate,repairItem.ReturnDate ,repairorders.TotalBillAmount
    FROM repairItem
    INNER JOIN repairorders ON repairItem.BillNumber=repairorders.BillNumber
    WHERE repairItem.BillNumber =  $P{lblOrderId};

SELECT Item.ItemCode, Item.Description, orderDetail.OrderQty, Item.warrantyPeriod,orderDetail.ItemTotal,orderDetail.ItemProfit
    FROM Item
    INNER JOIN orderDetail ON Item.ItemCode=orderDetail.ItemCode
    WHERE OrderId = 'O-00010';

SELECT Customer.CustomerId, Customer.CustomerName, Customer.Email, Customer.PhoneNumber
FROM Customer
INNER JOIN Orders ON Orders.CustomerId=Customer.CustomerId
WHERE OrderId='O-00001';

SELECT repairitem. Repair_ItemCode, repairitem.BillNumber, repairitem.Description, repairorders.orderId, repairorders.
FROM Customer
INNER JOIN Orders ON Orders.CustomerId=Customer.CustomerId
WHERE OrderId='O-00001';

------------------------------------------------------------------------------

SELECT  OrderProfit
FROM orders
WHERE MONTH (OrderDate) ='9';

SELECT SUM(OrderProfit) AS "MonthlyProfit"
FROM orders
WHERE monthname(curdate()) ='September'; // this

select monthname(curdate()) FROM orders; // rightsssssss.........

DECLARE @date datetime2 = '2021-08-17';
SELECT FORMAT(@date, 'MMMM') AS "Result";

------------------------------------------------------------------------------

SELECT  OrderProfit
FROM orders
WHERE FORMAT(@date, 'MMMM');

SELECT OrderDate(month, GETDATE()) AS "Month Name" FROM orders;

SELECT  OrderDate(TO_DATE(9, 'MM'), 'OrderDate') AS "Month Name" FROM orders;

/*--------------------------------------------------------------------*/

SELECT Orders.OrderID, Customers.CustomerName, Shippers.ShipperName
FROM ((Orders
INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID)
INNER JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID);

SELECt Patients.empId, Patients.nIC, Patients.name, patients.contactNo, patients.age, Appoinment.appoinmentId, Appoinment.drId,Payment.total
FROM((Patiennts
INNER JOIN Patients ON Patiennts.nIC = Appoinment.nIC)
INNER JOIN Payment ON Patiennts.nIC = Payment.nIC )


////////////////////////////////////////////////////////////////////////

SELECT OrderProfit FROM orders WHERE MONTH (OrderDate) = '9';

/*---------------------------------------------------------------------*/

select itemCode, SUM(itemProfit) from orderDetail Group by ItemProfit; // good

select itemCode, SUM(itemProfit) from orderDetail Group by ItemProfit Order by itemProfit DESC Limit 6; // better

select item.Description, SUM(orderDetail.itemProfit)
from orderDetail
inner join item on orderDetail.itemCode = item.itemCode
Group by orderDetail.ItemProfit Order by orderDetail.itemProfit DESC Limit 6;

/////////////////////////////////////////////////////////////////////////

 select itemCode, SUM(itemProfit) from orderdetail group by itemcode order by itemprofit desc limit 7;

 select orderdetail.itemCode, SUM(orderdetail.itemProfit), item.agentId
 from orderdetail
 inner join item on orderDetail.itemCode = item.itemCode
 group by itemcode
 order by itemprofit desc limit 7;


 select   agent.companyName, item.agentId, SUM(orderdetail.itemTotal) as 'TotalSales'
 from orderdetail
 inner join item on orderDetail.itemCode = item.itemCode
 inner join agent on agent.agentId = item.agentId
 group by agent.companyName
 order by TotalSales asc limit 7;


eg:
SELECT
  student.first_name,
  student.last_name,
  course.name
FROM student
JOIN student_course
  ON student.id = student_course.student_id
JOIN course
  ON course.id = student_course.course_id;


eg:

SELECT agents.agent_code,agents.agent_name,
SUM(orders.advance_amount)
FROM agents,orders
WHERE agents.agent_code=orders.agent_code
GROUP BY agents.agent_code,agents.agent_name
ORDER BY agents.agent_code;


//======================================================================

select reservationnote.ReservationNo, reservationnote.CustomerId, customer.CustomerName, customer.email, customer.phoneNumber, reservationNote.contexts, reservationNote.ReservationDate
from reservationNote
inner join customer on reservationNote.CustomerId = customer.CustomerId;

select users.UserName, SUM(orders.AbsoluteTotal)
from orders
inner join users on orders.UserId = users.UserId
group by users.userId;

select repairItem.Repair_ItemCode , repairItem.BillNumber , repairItem.OrderId , repairItem.Description , repairItem. , repairItem.RecievedDate
 , repairItem.ReturnDate , repairItem.externalCharge , repairOrders.
from orders
inner join users on orders.UserId = users.UserId
group by users.userId;


//-----------------------------------------------------------------------


