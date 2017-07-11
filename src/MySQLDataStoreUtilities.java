import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MySQLDataStoreUtilities {
	private final static String DROP_REGISTRATION_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS Registration";
	private final static String DROP_ORDERS_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS Orders";
	private final static String DROP_ORDER_ITEMS_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS OrderItems";
	private final static String CREATE_REGISTRATION_TABLE = "CREATE TABLE IF NOT EXISTS Registration (userID INT NOT NULL AUTO_INCREMENT, userName VARCHAR(255) NOT NULL, password VARCHAR(255), userType VARCHAR(255), PRIMARY KEY(userID));";
	private final static String CREATE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS Orders (orderID INT NOT NULL AUTO_INCREMENT, userID INT NOT NULL, orderName VARCHAR(255), orderPrice DOUBLE, userAddress VARCHAR(255), creditCardNo VARCHAR(255), deliveryDate VARCHAR(255), PRIMARY KEY(orderID), FOREIGN KEY (userID) REFERENCES Registration(userID));";
	private final static String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS OrderItems (itemID VARCHAR(255) NOT NULL, orderID INT NOT NULL, itemType VARCHAR(255), itemName VARCHAR(255), itemPrice DOUBLE, itemImage VARCHAR(255), itemRetailer VARCHAR(255), itemDiscount DOUBLE, itemAmount INT, itemExtraCost DOUBLE, PRIMARY KEY(itemID, orderID), FOREIGN KEY (orderID) REFERENCES Orders(orderID));";
	private final static String ADD_USER = "INSERT INTO Registration (userName, password, userType) VALUES (?, ?, ?);";
	private final static String GET_USER = "SELECT * FROM Registration WHERE userName = ?";
	private final static String GET_ALL_USERIDS_AND_USERNAMES = "SELECT userID, userName FROM Registration";
	private final static String GET_USER_ID_BY_NAME = "SELECT userID FROM Registration WHERE userName = ?";
	private final static String GET_USER_NAME_BY_ID = "SELECT userName FROM Registration WHERE userID = ?";
	private final static String ADD_ORDER = "INSERT INTO Orders (userID, orderName, orderPrice, userAddress, creditCardNo, deliveryDate) VALUES (?, ?, ?, ?, ?, ?);";
	private final static String GET_ALL_ORDERS = "SELECT * FROM Orders;";
	private final static String GET_ORDER_BY_ID = "SELECT * FROM Orders WHERE orderID = ?;";
	private final static String DELETE_ORDER_BY_ID = "DELETE FROM Orders WHERE orderID = ?;";
	private final static String UPDATE_ORDER_BY_ID = "UPDATE Orders SET userAddress = ?, creditCardNo = ?, deliveryDate = ? WHERE orderID = ?;";
	private final static String GET_ORDERS_TABLE_STATUS = "SHOW TABLE STATUS LIKE 'Orders';";
	private final static String ADD_ORDER_ITEM = "INSERT INTO OrderItems (itemID, orderID, itemType, itemName, itemPrice, itemImage, itemRetailer, itemDiscount, itemAmount, itemExtraCost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private final static String GET_ORDER_ITEMS_BY_ORDER_ID = "SELECT * FROM OrderItems WHERE orderID = ?;";
	private final static String DELETE_ORDER_ITEMS_BY_ORDER_ID = "DELETE FROM OrderItems WHERE orderID = ?;";

	public static void initalizeDatabase() {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			Statement s = connection.createStatement();
			s.executeUpdate(DROP_ORDER_ITEMS_TABLE_IF_EXISTS);
			s.executeUpdate(DROP_ORDERS_TABLE_IF_EXISTS);
			s.executeUpdate(DROP_REGISTRATION_TABLE_IF_EXISTS);
			s.executeUpdate(CREATE_REGISTRATION_TABLE);
			s.executeUpdate(CREATE_ORDERS_TABLE);
			s.executeUpdate(CREATE_ORDER_ITEMS_TABLE);
			PreparedStatement ps = connection.prepareStatement(ADD_USER);
			ps.setString(1, "aa");
			ps.setString(2, "aa");
			ps.setString(3, "customer");
			ps.executeUpdate();
			ps.setString(1, "as");
			ps.setString(2, "as");
			ps.setString(3, "retailer");
			ps.executeUpdate();
			ps.setString(1, "ad");
			ps.setString(2, "ad");
			ps.setString(3, "manager");
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static User getUser(String userName) {
		User user = null;
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_USER);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User(rs.getString("userName"), rs.getString("password"), rs.getString("userType"));
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static void addUser(User user) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(ADD_USER);
			ps.setString(1, user.getName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getUsertype());
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Integer, String> getAllUserIDsAndNames() {
		HashMap<Integer, String> userIDNameMap = new HashMap<Integer, String>();
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_ALL_USERIDS_AND_USERNAMES);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int numberOfRows = rs.getRow();
			rs.beforeFirst();
			while (numberOfRows > 0 && rs.next()) {
				Integer userID = rs.getInt("userID");
				String userName = rs.getString("userName");
				userIDNameMap.put(userID, userName);
				numberOfRows--;
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userIDNameMap;
	}

	public static int getUserIDByName(String userName) {
		int userID = -1;
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_USER_ID_BY_NAME);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userID = rs.getInt("userID");
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}

	public static String getUserNameByID(int userID) {
		String userName = null;
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_USER_NAME_BY_ID);
			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userName = rs.getString("userName");
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userName;
	}

	public static void addOrder(OrderPayment op) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(ADD_ORDER);
			ps.setInt(1, op.getUserID());
			ps.setString(2, op.getOrderName());
			ps.setDouble(3, op.getOrderPrice());
			ps.setString(4, op.getUserAddress());
			ps.setString(5, op.getCreditCardNo());
			ps.setString(6, op.getDeliveryDate());
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getNextOrderID() {
		int nextOrderID = -1;
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_ORDERS_TABLE_STATUS);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				nextOrderID = rs.getInt("Auto_increment");
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nextOrderID;
	}

	public static void addOrderItem(OrderItem oi, int orderID) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(ADD_ORDER_ITEM);
			ps.setString(1, oi.getItemID());
			ps.setInt(2, orderID);
			ps.setString(3, oi.getType());
			ps.setString(4, oi.getName());
			ps.setDouble(5, oi.getPrice());
			ps.setString(6, oi.getImage());
			ps.setString(7, oi.getRetailer());
			ps.setDouble(8, oi.getDiscount());
			ps.setInt(9, oi.getAmount());
			ps.setDouble(10, oi.getExtraCost());
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static OrderPayment getOrderByID(int orderID) {
		OrderPayment op = null;
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_ORDER_BY_ID);
			ps.setInt(1, orderID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				op = new OrderPayment(orderID, rs.getInt("userID"), rs.getString("orderName"),
						rs.getDouble("orderPrice"), rs.getString("userAddress"), rs.getString("creditCardNo"),
						rs.getString("deliveryDate"));
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return op;
	}

	public static ArrayList<OrderPayment> getAllOrders() {
		ArrayList<OrderPayment> orderPaymentList = new ArrayList<OrderPayment>();
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_ALL_ORDERS);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int numberOfRows = rs.getRow();
			rs.beforeFirst();
			while (numberOfRows > 0 && rs.next()) {
				OrderPayment op = new OrderPayment(rs.getInt("orderID"), rs.getInt("userID"), rs.getString("orderName"),
						rs.getDouble("orderPrice"), rs.getString("userAddress"), rs.getString("creditCardNo"),
						rs.getString("deliveryDate"));
				orderPaymentList.add(op);
				numberOfRows--;
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderPaymentList;
	}

	public static ArrayList<OrderItem> getOrderItemsByOrderID(int orderID) {
		ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(GET_ORDER_ITEMS_BY_ORDER_ID);
			ps.setInt(1, orderID);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int numberOfRows = rs.getRow();
			rs.beforeFirst();
			while (numberOfRows > 0 && rs.next()) {
				OrderItem oi = new OrderItem(rs.getString("itemID"), rs.getInt("orderID"), rs.getString("itemType"),
						rs.getString("itemName"), rs.getDouble("itemPrice"), rs.getString("itemImage"),
						rs.getString("itemRetailer"), rs.getDouble("itemDiscount"), rs.getInt("itemAmount"),
						rs.getDouble("itemExtraCost"));
				orderItemList.add(oi);
				numberOfRows--;
			}
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderItemList;
	}

	public static void deleteOrderItemsByOrderID(int orderID) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_ORDER_ITEMS_BY_ORDER_ID);
			ps.setInt(1, orderID);
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderByID(int orderID) {
		deleteOrderItemsByOrderID(orderID);
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_ORDER_BY_ID);
			ps.setInt(1, orderID);
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderByID(int orderID, String userAddress, String creditCardNo, String deliveryDate) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			PreparedStatement ps = connection.prepareStatement(UPDATE_ORDER_BY_ID);
			ps.setString(1, userAddress);
			ps.setString(2, creditCardNo);
			ps.setString(3, deliveryDate);
			ps.setInt(4, orderID);
			ps.executeUpdate();
			ConnectionPool.getInstance().freeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
