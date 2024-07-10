package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class ReadWordContent {



    /**
     * word doc的标题格式前缀，标题 styleName： 标题 1，标题 2，...
     * 正文 为 "正文"
     */
    private static final String docTitlePrefix = "标题 ";

    private static final String docText = "正文";

    /**
     * word docx的标题格式前缀，标题 styleName： heading 1，heading 2，...;
     * 正文 为 null
     */
    private static final String docXTitlePrefix = "heading ";

    private static final String structTitlePrefix = "h";

    private static final String structText = "p";

    /**
     * 通过XWPFWordExtractor访问XWPFDocument的内容
     * @throws Exception
     */
    public void readByExtractor(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XWPFDocument doc = new XWPFDocument(is);
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        String text = extractor.getText();
        System.out.println(text);
        /**
         * 打印文档信息
         */
        POIXMLProperties.CoreProperties coreProps = extractor.getCoreProperties();
        this.printCoreProperties(coreProps);
        this.close(is);
    }

    /**
     * 输出CoreProperties信息
     * @param coreProps
     */
    private void printCoreProperties(POIXMLProperties.CoreProperties coreProps) {
        String category = coreProps.getCategory(); //分类
        String creator = coreProps.getCreator(); //创建者,Microsoft Office User
        Date createdDate = coreProps.getCreated(); //创建时间
        String title = coreProps.getTitle(); //标题
        String description = coreProps.getDescription(); //描述，默认为null

        System.out.println(category);
        System.out.println(creator);
        System.out.println(createdDate);
        System.out.println(title);
        System.out.println(description);
    }

    /**
     * 关闭输入流
     * @param is
     */
    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public List<StyleTextVO> readDoc(String path) throws Exception {
        List<StyleTextVO> styleTextVOList = new ArrayList<>();
        InputStream is = new FileInputStream(path);
        HWPFDocument doc = new HWPFDocument(is);
        Range r = doc.getRange();

        for (int i = 0; i < r.numParagraphs(); i++) {
            Paragraph p = r.getParagraph(i);
            int styleIndex = p.getStyleIndex();

            StyleSheet style_sheet = doc.getStyleSheet();
            StyleDescription style = style_sheet.getStyleDescription(styleIndex);
            String styleName = style.getName();
            int titleLevel = -1; // 默认为正文
            if (styleName.equals(docText)){
                styleName = structText;
            }else{
                titleLevel = styleIndex;
                styleName = styleName.replace(docTitlePrefix, structTitlePrefix);
            }

            StyleTextVO styleTextVO = StyleTextVO.builder()
                    .titleLevel(titleLevel).style(styleName).text(p.text()).build();
            styleTextVOList.add(styleTextVO);
        }

        doc.close();

        return styleTextVOList;
    }


    /**
     * 读取docx内容
     * @param path
     * @throws Exception
     */
    public List<StyleTextVO> readDocX(String path) throws Exception {
        List<StyleTextVO> styleTextVOList = new ArrayList<>();

        InputStream is = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(is);

        List<XWPFParagraph> paragraphList = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphList){
            String styleId = paragraph.getStyleID();

            String styleName = "正文";
            int titleLevel = -1;
            if (!StringUtils.isEmpty(styleId)){
                try{
                    titleLevel = Integer.valueOf(styleId);
                    XWPFStyle style = document.getStyles().getStyle(styleId);
                    styleName = style.getName();
                }catch (Exception e){
                    log.error("不支持的标题格式, msg:{}", e.getMessage());
                }
            }

            if (styleId == null){
                styleName = structText;
            }else{
                styleName = styleName.replace(docXTitlePrefix, structTitlePrefix);
            }
            StyleTextVO styleTextVO = StyleTextVO.builder()
                    .titleLevel(titleLevel).style(styleName).text(paragraph.getText()).build();
            styleTextVOList.add(styleTextVO);
        }

        this.close(is);

        return styleTextVOList;
    }


}