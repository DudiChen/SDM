//package builder;
//
//import entity.Area;
//import entity.Customer;
//import entity.market.Market;
//import jaxb.generated.*;
//import util.ErrorMessage;
//
//import javax.xml.bind.ValidationException;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//import builder.validate.DataValidationUtils.*;
//
//import static builder.validate.DataValidationUtils.preliminaryDataValidation;
//
//public class MarketBuilder implements Builder<SuperDuperMarketDescriptor, Market>{
//    Map<Integer, Area> idToArea;
//    Map<Integer, Customer> idToCustomer;
//
//    @Override
//    public Market build(SuperDuperMarketDescriptor source) throws ValidationException {
//        idToCustomer = new HashMap<>();
//        preliminaryDataValidation(source); // Performs all preliminary data validation checking invalid values
//
//
//
//    }
//
//
//
////    private Map<Integer, Customer> getIdToCustomer(List<SDMCustomer> sdmCustomers) {
////        return constructIdToCustomer(new HashSet<>(sdmCustomers));
////    }
//
////    private Map<Integer, Customer> constructIdToCustomer(HashSet<SDMCustomer> sdmCustomers) {
////        return sdmCustomers.stream().collect(Collectors.toMap(SDMCustomer::getId, sdmCustomer -> new CustomerBuilder().build(sdmCustomer)));
////    }
//}
