package org.javaacademy.mapstruct_homework.mapper;

import org.javaacademy.mapstruct_homework.dto.PersonCreditDto;
import org.javaacademy.mapstruct_homework.dto.PersonDriverLicenceDto;
import org.javaacademy.mapstruct_homework.dto.PersonInsuranceDto;
import org.javaacademy.mapstruct_homework.entity.Human;
import org.javaacademy.mapstruct_homework.util.NoNullPredicate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper()
public interface HumanMapper {
    @Mapping(target = "passportNumber", source = ".", qualifiedByName = "getPassportNumber")
    @Mapping(target = "salary", source = ".", qualifiedByName = "getSalary")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    PersonCreditDto convertToCreditDto(Human human);

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullPassportData", source = ".", qualifiedByName = "getFullPassportData")
    @Mapping(target = "birthDate", source = ".", qualifiedByName = "getBirthDate")
    PersonDriverLicenceDto convertToDriverLicenceDto(Human human);

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    @Mapping(target = "fullAge", source = ".", qualifiedByName = "getFullAge")
    PersonInsuranceDto convertToInsuranceDto(Human human);

    @Named("getPassportNumber")
    default String getPassportNumber(Human human) {
        return "%s%s".formatted(
                human.getPassport().getSeries(),
                human.getPassport().getNumber());
    }

    @Named("getSalary")
    default String getSalary(Human human) {
        return "%s %s".formatted(
                human.getWork().getSalary(),
                human.getWork().getCurrency());
    }

    @Named("getFullAddress")
    default String getFullAddress(Human human) {
        Predicate<String> noNullPredicate = new NoNullPredicate();
        return Stream.of(
                        human.getLivingAddress().getRegion(),
                        human.getLivingAddress().getCity(),
                        human.getLivingAddress().getStreet(),
                        human.getLivingAddress().getHouse(),
                        human.getLivingAddress().getBuilding(),
                        human.getLivingAddress().getFlat())
                .filter(noNullPredicate)
                .collect(Collectors.joining(" "));
    }

    @Named("getFullName")
    @Mapping(target = "middleName", source = "human.middleName", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
    default String getFullName(Human human) {
        Predicate<String> noNullPredicate = new NoNullPredicate();
        return Stream.of(
                human.getFirstName(),
                human.getLastName(),
                human.getMiddleName())
                .filter(noNullPredicate)
                .collect(Collectors.joining(" "));
    }

    @Named("getFullPassportData")
    default String getFullPassportData(Human human) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        return "%s %s".formatted(
                getPassportNumber(human),
                human.getPassport().getIssueDate().format(formatter));
    }

    @Named("getBirthDate")
    default String getBirthDate(Human human) {
        return "%s.%s.%s".formatted(
                human.getBirthDay(),
                human.getBirthMonth(),
                human.getBirthYear());
    }

    @Named("getFullAge")
    default Integer getFullAge(Human human) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate birthDate = LocalDate.parse(getBirthDate(human), formatter);
        return birthDate.until(LocalDate.now()).getYears();
    }
}
