package com.icici.dma.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class DateFormatter {

	private static final List<DateTimeFormatter> LOCAL_DATE_FORMATTERS = Arrays.asList(

			// ===== ISO (Fastest & Most Common) =====
			DateTimeFormatter.ISO_LOCAL_DATE,

			// ===== Daily Used =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MM-yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MM/dd/yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy/MM/dd")
					.toFormatter(Locale.ENGLISH),

			// ===== d/M Variations =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-M-yyyy").toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-M-yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-MM-yyyy")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d/M/yyyy").toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/M/yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d/MM/yyyy")
					.toFormatter(Locale.ENGLISH),

			// ===== Month Name =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d MMM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-MMM-yyyy")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMMM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d MMMM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMMM-yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-MMMM-yyyy")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM dd, yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM d, yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMMM dd, yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMMM d, yyyy")
					.toFormatter(Locale.ENGLISH),

			// ===== Dot =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd.MM.yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d.MM.yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd.M.yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d.M.yyyy").toFormatter(Locale.ENGLISH),

			// ===== Space =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d MM yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd M yyyy")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d M yyyy").toFormatter(Locale.ENGLISH),

			// ===== Compact =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyyMMdd").toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("ddMMyyyy").toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMddyyyy")
					.toFormatter(Locale.ENGLISH));

	private static final List<DateTimeFormatter> LOCAL_DATE_TIME_FORMATTERS = Arrays.asList(

			// ===== ISO (Most Common) =====
			DateTimeFormatter.ISO_LOCAL_DATE_TIME,

			// ===== Daily Used =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MM-yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MM-yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MM/dd/yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MM/dd/yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy/MM/dd HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy/MM/dd HH:mm")
					.toFormatter(Locale.ENGLISH),

			// ===== d/M Variations =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-M-yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d-M-yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d/M/yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d/M/yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			// ===== Month Name =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMM yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMM yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy HH:mm")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd MMMM yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),

			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM dd, yyyy HH:mm:ss")
					.toFormatter(Locale.ENGLISH),

			// ===== Milliseconds =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.S")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.SS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.SSS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.SSSS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
					.toFormatter(Locale.ENGLISH),

			// ===== ISO =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd'T'HH:mm:ss")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
					.toFormatter(Locale.ENGLISH),

			// ===== AM / PM =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yyyy hh:mm:ss a")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MM-yyyy hh:mm:ss a")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd hh:mm:ss a")
					.toFormatter(Locale.ENGLISH),

			// ===== Oracle =====
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yy HH.mm.ss.SSSSSS a")
					.toFormatter(Locale.ENGLISH),
			new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy HH.mm.ss.SSSSSS a")
					.toFormatter(Locale.ENGLISH));

	public static LocalDate parseToLocalDate(String value) {

		if (value == null) {
			return null;
		}

		value = value.trim();

		if (value.isEmpty() || "NULL".equalsIgnoreCase(value) || "NA".equalsIgnoreCase(value)
				|| "N/A".equalsIgnoreCase(value) || "-".equals(value) || "--".equals(value)) {
			return null;
		}

		for (DateTimeFormatter formatter : LOCAL_DATE_FORMATTERS) {
			try {
				return LocalDate.parse(value, formatter);
			} catch (DateTimeParseException ignored) {
			}
		}

		throw new IllegalArgumentException("Unsupported date format: '" + value + "'");
	}

	public static LocalDateTime parseToLocalDateTime(String value) {

		if (value == null) {
			return null;
		}

		value = value.trim();

		if (value.isEmpty() || "NULL".equalsIgnoreCase(value) || "NA".equalsIgnoreCase(value)
				|| "N/A".equalsIgnoreCase(value) || "-".equals(value) || "--".equals(value)) {
			return null;
		}

		for (DateTimeFormatter formatter : LOCAL_DATE_TIME_FORMATTERS) {

			try {
				return LocalDateTime.parse(value, formatter);
			} catch (DateTimeParseException ignored) {
			}
		}

		throw new IllegalArgumentException("Unsupported date-time format: '" + value + "'");
	}
}
