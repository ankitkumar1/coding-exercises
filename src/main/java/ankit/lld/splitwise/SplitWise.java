package ankit.lld.splitwise;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SplitWise {

    public static void main(String[] args) {
        SplitWiseService service = new SplitWiseService();
        User ankit = service.addUser("Ankit");
        User mohit = service.addUser("Mohit");
        User sunil = service.addUser("Sunil");
        User dinesh = service.addUser("Dinesh");
        User sanjay = service.addUser("Sanjay");

        Group goaTrip = service.createGroup("Goa Trip", ankit, List.of(mohit, sunil, sanjay, dinesh));

        Expense expense = new Expense("Breakfast", 1000,
                goaTrip, ankit, List.of(new Split(mohit, 200), new Split(sunil, 200),
                new Split(dinesh, 200), new Split(sanjay, 200)));

        service.addExpenseToGroup(goaTrip, expense);

        Expense expense2 = new Expense("Lunch", 2000,
                goaTrip, mohit, List.of(new Split(ankit, 400), new Split(sunil, 400),
                new Split(dinesh, 400), new Split(sanjay, 400)));

        service.addExpenseToGroup(goaTrip, expense2);

        service.showGroupBalanceSheet(goaTrip);
        System.out.println();
        service.showUserBalanceSheet(ankit);
    }

}

class SplitWiseService{

    Map<Integer, Group> groupMap = new ConcurrentHashMap<>();
    Map<Integer, User> userMap = new ConcurrentHashMap<>();
    BalanceSheet balanceSheet = new BalanceSheet();
    Map<Group, ReentrantLock> groupLocks = new ConcurrentHashMap<>();
    
    public SplitWiseService(){

    }

    public User addUser(String name){
        User user = new User(name);
        this.userMap.put(user.id, user);
        this.balanceSheet.userBalances.put(user, new HashMap<>());
        System.out.println("User Id: "+user.id+", Name: "+user.name+" successfully created");
        return user;
    }

    public Group createGroup(String groupName, User creator, List<User> users){
        Group group = new Group(groupName, creator, users);
        this.groupMap.put(group.id, group);
        this.balanceSheet.groupBalances.put(group, new HashMap<>());
        // Put entry for all the users in group.
        this.balanceSheet.groupBalances.get(group).put(creator, new HashMap<>());
        users.forEach(user -> this.balanceSheet.groupBalances.get(group).put(user, new HashMap<>()));
        System.out.println("Group Id: "+group.id+", Name: "+groupName+" successfully created");
        return group;
    }

    public void addUserToGroup(Integer groupId, Integer userId){
        Group group = groupMap.get(groupId);
        User user = userMap.get(userId);
        group.addUsers(List.of(user));
        this.balanceSheet.groupBalances.get(group).put(user, new HashMap<>());
        System.out.println("User "+user.name+" is added successfully to group "+ group.name);
    }

    public void addExpenseForSingleUser(User creditor, User debtor, double amount){
        Map<User, Double> creditorMap = balanceSheet.userBalances.get(creditor);
        Map<User, Double> debtorMap = balanceSheet.userBalances.get(debtor);
        debtorMap.put(creditor, debtorMap.getOrDefault(creditor, 0d) + amount);
        creditorMap.put(debtor, creditorMap.getOrDefault(debtor, 0d) - amount);
    }

    public void addExpenseToGroup(Group group, Expense expense){
        groupLocks.putIfAbsent(group, new ReentrantLock());
        groupLocks.get(group).lock();
        try{
            // Step1: Add expense to group
            expense.group.addExpense(expense);
            Map<User, Map<User, Double>> groupBalance = balanceSheet.groupBalances.get(group);
            User payer = expense.paidBy;

            // Step2: Update user balance sheet and group balance sheet
            for(Split split : expense.splits){
                if(split.user() == payer){
                    continue;
                }
                User debtor = split.user();
                Map<User, Double> debtorMap = balanceSheet.userBalances.get(debtor);
                Map<User, Double> creditorMap = balanceSheet.userBalances.get(payer);

                debtorMap.put(payer, debtorMap.getOrDefault(payer, 0d) + split.amount());
                creditorMap.put(debtor, creditorMap.getOrDefault(debtor, 0d) - split.amount());

                if(groupBalance != null){
                    Map<User, Double> groupDebtorMap = groupBalance.get(debtor);
                    groupDebtorMap.put(payer, groupDebtorMap.getOrDefault(payer, 0d) + split.amount());
                }
            }
        }finally {
            groupLocks.get(group).unlock();
        }
    }

    public void showUserBalanceSheet(User user){
        this.balanceSheet.userBalances.get(user).forEach((user1, amount) -> {
            if(amount < 0){
                System.out.println(user.name+" gets "+ -amount +" from "+ user1.name);
            }else if(amount > 0){
                System.out.println(user.name+" ows "+ amount +" to "+ user1.name);
            }
        });
    }

    public void showGroupBalanceSheet(Group group){
        simplifyDebt(group);
    }

    private void simplifyDebt(Group group){
        Map<User, Map<User, Double>> userBalances = balanceSheet.groupBalances.get(group);
        Map<User, Double> netBalances = new HashMap<>();
        userBalances.forEach((debtor, innerMap) -> {
            innerMap.forEach((creditor, amount) -> {
                netBalances.put(debtor, netBalances.getOrDefault(debtor, 0d) - amount);
                netBalances.put(creditor, netBalances.getOrDefault(creditor, 0d) + amount);
            });
        });

        PriorityQueue<Map.Entry<User, Double>> creditorHeap =
                new PriorityQueue<>((a,b) -> Double.compare(b.getValue(), a.getValue()));

        PriorityQueue<Map.Entry<User, Double>> debtorHeap =
                new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));

        netBalances.entrySet().forEach(e -> {
            if(e.getValue() > 0){
                creditorHeap.offer(e);
            }else{
                debtorHeap.offer(e);
            }
        });

        StringBuilder builder = new StringBuilder();
        while(!creditorHeap.isEmpty() && !debtorHeap.isEmpty()){
            Map.Entry<User, Double> topCreditor = creditorHeap.poll();
            Map.Entry<User, Double> topDebtor = debtorHeap.poll();
            double settledAmount = Math.min(topCreditor.getValue(), - topDebtor.getValue());
            builder.append("\n").append(topDebtor.getKey().name).append(" pays ")
                    .append(settledAmount).append(" to ").append(topCreditor.getKey().name);
            if(topCreditor.getValue() - settledAmount > 0){
                topCreditor.setValue(topCreditor.getValue() - settledAmount);
                creditorHeap.offer(topCreditor);
            }

            if(settledAmount + topDebtor.getValue() < 0){
                topDebtor.setValue(settledAmount + topDebtor.getValue()); // Its basically subtracting.
                debtorHeap.offer(topDebtor);
            }
        }
        System.out.println(builder);
    }
}

class User{
    int id;
    String name;
    User(String name){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
    }

    @Override
    public String toString(){
        return id+"_"+name;
    }
}

class Group{
    int id;
    String name;
    User creator;
    Set<User> users;
    List<Expense> expenses;

    Group(String name, User creator, List<User> users){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
        this.creator = creator;
        this.users = new HashSet<>();
        this.users.add(creator);
        if(users != null){
            this.users.addAll(users);
        }
        this.expenses = new ArrayList<>();
    }

    public void addUsers(List<User> users){
        this.users.addAll(users);
    }

    public void addExpense(Expense expense){
        this.expenses.add(expense);
    }
}

class Expense{
    int id;
    String description;
    double totalAmount;
    Group group;  // Optional
    User paidBy;
    List<Split> splits;

    Expense(String description,  double totalAmount, Group group, User paidBy, List<Split> splits){
        this.id = Util.idGenerator.incrementAndGet();
        this.description = description;
        this.totalAmount = totalAmount;
        this.group = group;
        this.paidBy = paidBy;
        this.splits = splits;
    }
}

record Split(User user, double amount){ }

class BalanceSheet{
    Map<User, Map<User, Double>> userBalances;  // User -> User,amount
    Map<Group, Map<User, Map<User, Double>>> groupBalances;  // Group -> User -> User,amount
    BalanceSheet(){
        this.userBalances = new ConcurrentHashMap<>();
        this.groupBalances = new ConcurrentHashMap<>();
    }
}

class Util{
    public static final AtomicInteger idGenerator = new AtomicInteger();
}



