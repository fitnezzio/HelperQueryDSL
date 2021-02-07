package io.fitnezz.helper.querydsl.filter;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import io.fitnezz.helper.querydsl.exception.FilterException;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class GenericFilter<T> {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private EntityPathBase<T> entityPathBase;
    private BooleanExpression booleanExpression;

    public GenericFilter(EntityPathBase<T> entityPathBase) {
        this.entityPathBase = entityPathBase;
    }

    public BooleanExpression getBooleanExpression() {
        return this.booleanExpression;
    }

    public void setBooleanExpression(BooleanExpression booleanExpression) {
        this.booleanExpression = Objects.isNull(this.booleanExpression)
            ? booleanExpression
            : this.booleanExpression.and(booleanExpression);
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Field getField(String fieldName) {
        try {
            return entityPathBase.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new FilterException("FILTER_ERROR_FIELD_NOT_FOUND", e.getCause());
        }
    }

    public Date convertStringToDate(String dateText) {
        try {
            return dateFormat.parse(dateText);
        } catch (ParseException e) {
            throw new FilterException("FILTER_ERROR_INVALID_DATE", e.getCause());
        }
    }

    public Object getPathObjectField(Field field) {
        try {
            return field.get(entityPathBase);
        } catch (IllegalAccessException e) {
            throw new FilterException("FILTER_ERROR_ATTR_NOT_FOUND", e.getCause());
        }
    }

    public void setContains(String fieldName, String value) {
        Field field = getField(fieldName);
        Object pathObjectField = getPathObjectField(field);

        if (field.getType().equals(StringPath.class)) {
            setTextContainsIgnoreCase((StringPath) pathObjectField, value);
        } else {
            throw new FilterException("FILTER_ERROR_CLASS_IMPL_NOT_FOUND");
        }
    }

    public void setDateIsNull(DateTimePath<Date> datePath, String isNullValue) {
        if (isNullValue != null) {
            setBooleanExpression(Boolean.parseBoolean(isNullValue) ? datePath.isNull() : datePath.isNotNull());
        }
    }

    public void setDateEquals(DateTimePath<Date> datePath, String dateValue) {
        if (dateValue != null) {
            Date date = convertStringToDate(dateValue);
            setBooleanExpression(datePath.eq(date));
        }
    }

    public void setDateIntervalStartEnd(DateTimePath<Date> datePath, String startDateValue, String endDateValue) {
        if (startDateValue != null || endDateValue != null) {
            Date startDate = null;
            Date endDate = null;

            if (startDateValue != null) {
                startDate = convertStringToDate(startDateValue);
            }

            if (endDateValue != null) {
                endDate = convertStringToDate(endDateValue);
            }

            if (startDate != null && endDate != null) {
                setBooleanExpression(datePath.between(startDate, endDate));
            } else if (startDate != null) {
                setBooleanExpression(datePath.goe(startDate));
            } else if (endDate != null) {
                setBooleanExpression(datePath.loe(endDate));
            }
        }
    }

    public void setTextEquals(StringPath stringPath, String text) {
        if (text != null) {
            setBooleanExpression(stringPath.eq(text));
        }
    }

    public void setTextContainsIgnoreCase(StringPath stringPath, String text) {
        if (text != null) {
            setBooleanExpression(stringPath.containsIgnoreCase(text));
        }
    }

    public void setTextOrTextConstainsIgnoreCase(StringPath stringPathOne, StringPath stringPathTwo, String text) {
        if (text != null) {
            BooleanExpression expressionOne = stringPathOne.containsIgnoreCase(text);
            BooleanExpression expressionTwo = stringPathTwo.containsIgnoreCase(text);
            setBooleanExpression(expressionOne.or(expressionTwo));
        }
    }

    public void setDoubleEquals(NumberPath<Double> doublePath, String doubleValue) {
        if (doubleValue != null) {
            setBooleanExpression(doublePath.eq(Double.valueOf(doubleValue)));
        }
    }

    public void setLongEquals(NumberPath<Long> longPath, String longValue) {
        if (longValue != null) {
            setBooleanExpression(longPath.eq(Long.valueOf(longValue)));
        }
    }

    public void setIntegerEquals(NumberPath<Integer> integerPath, String integerValue) {
        if (integerValue != null) {
            Integer integer = Integer.valueOf(integerValue);
            setBooleanExpression(integerPath.eq(integer));
        }
    }

    public void setBooleanEquals(BooleanPath booleanPath, Boolean booleanValue) {
        if (booleanValue != null) {
            setBooleanExpression(booleanPath.eq(booleanValue));
        }
    }

    public <E extends Enum<E>> void setEnumEquals(EnumPath<E> enumPath, Class<E> enumClass, String enumValue) {
        if (enumValue != null) {
            E enumerator = Enum.valueOf(enumClass, enumValue);
            setBooleanExpression(enumPath.eq(enumerator));
        }
    }

}
