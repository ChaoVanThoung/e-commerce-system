package views;

import model.dto.product.ProductResponseDto;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableUI<T> {
    private final CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);

    public void getTableDisplay(List<T> tList) {
        if (tList.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        if (tList.getFirst() instanceof ProductResponseDto) {
            // Group products by category
            Map<String, List<ProductResponseDto>> groupedByCategory = tList.stream()
                    .map(t -> (ProductResponseDto) t)
                    .collect(Collectors.groupingBy(ProductResponseDto::category));

            for (Map.Entry<String, List<ProductResponseDto>> entry : groupedByCategory.entrySet()) {
                String category = entry.getKey();
                List<ProductResponseDto> productsInCategory = entry.getValue();

                System.out.println("\nCategory: " + category.toUpperCase());

                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
                String[] columnNames = {"UUID", "NAME", "PRICE", "QUANTITY", "CATEGORY"};

                for (String column : columnNames) {
                    table.addCell(column, center);
                }

                for (ProductResponseDto product : productsInCategory) {
                    table.addCell(product.p_uuid(), center);
                    table.addCell(product.p_name(), center);
                    table.addCell(String.valueOf(product.price()), center);
                    table.addCell(product.qty().toString(), center);
                    table.addCell(product.category(), center);
                }

                for (int i = 0; i < columnNames.length; i++) {
                    table.setColumnWidth(i, 40, 45);
                }

                System.out.println(table.render());
            }
        } else {
            System.out.println("Unsupported data type.");
        }
    }
}
