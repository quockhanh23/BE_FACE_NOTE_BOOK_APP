package com.example.final_case_social_web.xml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.LinkedHashMap;

// https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml

public class ConvertXMLToJson {
    public static void main(String[] args) {
        try {

            String test =
                    "<report version=\"5.11.0\" xmlns=\"http://ws.cmctos.com.my/ctosnet/response\">\n" +
                            "    <enq_report id=\"BB-6043-20240510143219\" report_type=\"CTOS\" title=\"CTOS ENQUIRY\">\n" +
                            "        <header>\n" +
                            "            <user id=\"cgs_uat\">cgs_uat</user>\n" +
                            "            <company id=\"CGSUAT\">CGS International</company>\n" +
                            "            <account>CGSUAT</account>\n" +
                            "            <tel/>\n" +
                            "            <fax/>\n" +
                            "            <enq_date>2024-05-10</enq_date>\n" +
                            "            <enq_time>14:32:19</enq_time>\n" +
                            "            <enq_status code=\"1\">SUCCESS</enq_status>\n" +
                            "        </header>\n" +
                            "        <summary>\n" +
                            "            <enq_sum ptype=\"I\" pcode=\"11\" seq=\"1\">\n" +
                            "                <name>ABU BIN HARIS</name>\n" +
                            "                <ic_lcno/>\n" +
                            "                <nic_brno>870701054461</nic_brno>\n" +
                            "                <stat>1015</stat>\n" +
                            "                <dd_index>9541</dd_index>\n" +
                            "                <mphone_nos/>\n" +
                            "                <ref_no>12345</ref_no>\n" +
                            "                <dist_code/>\n" +
                            "                <purpose code=\"200\">Credit evaluation/account opening on subject/directors/shareholder with consent /due diligence on AMLA compliance</purpose>\n" +
                            "                <include_consent>0</include_consent>\n" +
                            "                <include_ctos>1</include_ctos>\n" +
                            "                <include_trex>0</include_trex>\n" +
                            "                <include_ccris sum=\"0\">0</include_ccris>\n" +
                            "                <include_dcheq>0</include_dcheq>\n" +
                            "                <include_ccris_supp>0</include_ccris_supp>\n" +
                            "                <include_fico>0</include_fico>\n" +
                            "                <include_ssm>0</include_ssm>\n" +
                            "                <include_ssm_retention>0</include_ssm_retention>\n" +
                            "                <include_angkasa>0</include_angkasa>\n" +
                            "                <include_etr_plus>2</include_etr_plus>\n" +
                            "                <confirm_entity/>\n" +
                            "                <enq_status code=\"1\">SUCCESS</enq_status>\n" +
                            "                <enq_code code=\"1\">CTOS Report</enq_code>\n" +
                            "            </enq_sum>\n" +
                            "        </summary>\n" +
                            "        <enquiry seq=\"1\">\n" +
                            "            <section_summary>\n" +
                            "                <ctos>\n" +
                            "                    <bankruptcy status=\"1\">\n" +
                            "                        <source code=\"M\" name=\"MDI\"/>\n" +
                            "                    </bankruptcy>\n" +
                            "                    <legal total=\"0\" value=\"0\"/>\n" +
                            "                    <legal_personal_capacity total=\"0\" value=\"0\"/>\n" +
                            "                    <legal_non_personal_capacity total=\"0\" value=\"0\"/>\n" +
                            "                </ctos>\n" +
                            "                <tr>\n" +
                            "                    <trex_ref negative=\"0\" positive=\"0\"/>\n" +
                            "                </tr>\n" +
                            "                <ccris>\n" +
                            "                    <application total=\"0\" approved=\"0\" pending=\"0\"/>\n" +
                            "                    <facility total=\"0\" arrears=\"0\" value=\"0\"/>\n" +
                            "                    <special_attention accounts=\"0\"/>\n" +
                            "                </ccris>\n" +
                            "                <dcheqs entity=\"0\"/>\n" +
                            "            </section_summary>\n" +
                            "            <section_a data=\"true\" title=\"SECTION A - IDENTITY NUMBER VERIFICATION\">\n" +
                            "                <record rpttype=\"Id\" seq=\"1\">\n" +
                            "                    <name match=\"1\">ABU BIN HARIS</name>\n" +
                            "                    <ic_lcno/>\n" +
                            "                    <nic_brno match=\"1\">870701054461</nic_brno>\n" +
                            "                    <addr>NO 61, USJ 1/4A, 47620, SUBANG JAYA, SELANGOR</addr>\n" +
                            "                    <addr_breakdown>\n" +
                            "                        <addr1>NO 61</addr1>\n" +
                            "                        <addr2>USJ 1/4A</addr2>\n" +
                            "                        <addr3>47620 SUBANG JAYA</addr3>\n" +
                            "                        <addr4>SELANGOR</addr4>\n" +
                            "                        <city>SUBANG JAYA</city>\n" +
                            "                        <state>SELANGOR</state>\n" +
                            "                        <postcode>47620</postcode>\n" +
                            "                        <country>MALAYSIA</country>\n" +
                            "                        <last_updated_date/>\n" +
                            "                    </addr_breakdown>\n" +
                            "                    <source>NRD</source>\n" +
                            "                    <cpo_date>12-11-2019</cpo_date>\n" +
                            "                    <remark/>\n" +
                            "                    <nationality>MALAYSIAN</nationality>\n" +
                            "                    <birth_date>01-07-1987</birth_date>\n" +
                            "                </record>\n" +
                            "            </section_a>\n" +
                            "            <section_b title=\"SECTION B - INTERNAL LIST\" data=\"true\">\n" +
                            "                <history seq=\"1\" rpttype=\"EH\" year=\"2024\">\n" +
                            "                    <period month=\"1\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"1\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"2\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"3\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"4\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"5\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"6\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"7\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"8\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"9\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"10\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"11\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"12\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                </history>\n" +
                            "                <history seq=\"2\" rpttype=\"EH\" year=\"2023\">\n" +
                            "                    <period month=\"1\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"2\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"3\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"4\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"5\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"6\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"7\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"8\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"9\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"10\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"11\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                    <period month=\"12\">\n" +
                            "                        <entity type=\"FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"NON-FI\" value=\"0\"/>\n" +
                            "                        <entity type=\"LAWYER\" value=\"0\"/>\n" +
                            "                    </period>\n" +
                            "                </history>\n" +
                            "            </section_b>\n" +
                            "            <section_c title=\"SECTION C - DIRECTORSHIPS AND BUSINESS INTERESTS\" data=\"true\">\n" +
                            "                <record rpttype=\"IF\" seq=\"1\">\n" +
                            "                    <company_name>1 PR SDN BHD</company_name>\n" +
                            "                    <additional_registration_no/>\n" +
                            "                    <local>5500001</local>\n" +
                            "                    <locala>P</locala>\n" +
                            "                    <object>STEEL FACTORY</object>\n" +
                            "                    <incdate>11-04-2018</incdate>\n" +
                            "                    <lastdoc>30-11-2019</lastdoc>\n" +
                            "                    <appoint>11-04-2018</appoint>\n" +
                            "                    <resign/>\n" +
                            "                    <name>ABU BIN HARIS</name>\n" +
                            "                    <ic_lcno/>\n" +
                            "                    <nic_brno>870701054461</nic_brno>\n" +
                            "                    <addr/>\n" +
                            "                    <position>DIRECTOR  /  SHARE HOLDER</position>\n" +
                            "                    <cpo_date>06-08-2020</cpo_date>\n" +
                            "                    <paidup>800000</paidup>\n" +
                            "                    <shares>120000</shares>\n" +
                            "                    <total_shares_percentage/>\n" +
                            "                    <remark>DIRECTOR 7</remark>\n" +
                            "                    <protif_after_tax>16132.00</protif_after_tax>\n" +
                            "                    <latest_financial_year>30-12-2017</latest_financial_year>\n" +
                            "                    <status>WINDING UP</status>\n" +
                            "                    <brn_ssm>5500001P</brn_ssm>\n" +
                            "                </record>\n" +
                            "            </section_c>\n" +
                            "            <section_d title=\"SECTION D\" data=\"true\">\n" +
                            "                <record rpttype=\"I1\" seq=\"1\" status=\"1\">\n" +
                            "                    <title>BANKRUPTCY - RECEIVING ORDER ISSUED</title>\n" +
                            "                    <special_remark>BANKRUPTCY - RECEIVING ORDER ISSUED</special_remark>\n" +
                            "                    <name match=\"1\">ABU BIN HARIS</name>\n" +
                            "                    <alias/>\n" +
                            "                    <addr>NO 61, USJ 1/4A, 47620, SUBANG JAYA, SELANGOR</addr>\n" +
                            "                    <ic_lcno/>\n" +
                            "                    <nic_brno match=\"1\">870701054461</nic_brno>\n" +
                            "                    <case_no>LIG-INC0219467-006</case_no>\n" +
                            "                    <court_detail>HIGH COURT KUALA LUMPUR</court_detail>\n" +
                            "                    <firm>AMLIFE SDN BHD KUALA LUMPUR</firm>\n" +
                            "                    <exparte>CIMB BANK</exparte>\n" +
                            "                    <notice>\n" +
                            "                        <date>12-05-0017</date>\n" +
                            "                        <source_detail>11-12-2018 BERITA HARIAN 11-12-2018</source_detail>\n" +
                            "                    </notice>\n" +
                            "                    <petition>\n" +
                            "                        <date>20-11-2020</date>\n" +
                            "                        <source_detail>BERTIA HARIAN 11-12-2018</source_detail>\n" +
                            "                    </petition>\n" +
                            "                    <order>\n" +
                            "                        <date>25-12-2020</date>\n" +
                            "                        <source_detail>UTUSAN MSIA 21-07-2019</source_detail>\n" +
                            "                    </order>\n" +
                            "                    <release/>\n" +
                            "                    <gazette_order>GAZETTE-6433445566</gazette_order>\n" +
                            "                    <gazette_notice/>\n" +
                            "                    <gazette_petition>GAZETTE-5817777841</gazette_petition>\n" +
                            "                    <gazette_discharge/>\n" +
                            "                    <hear_date>12-07-0006</hear_date>\n" +
                            "                    <amount>80000</amount>\n" +
                            "                    <remark>YAN WAI TEST DATA - INC0219467</remark>\n" +
                            "                    <lawyer>\n" +
                            "                        <name>JAYASANGAR &amp; CO</name>\n" +
                            "                        <add1>51-M,JALAN THAMBYPILLAI,BRICKFIELDS</add1>\n" +
                            "                        <add2>KUALA LUMPUR</add2>\n" +
                            "                        <add3/>\n" +
                            "                        <add4/>\n" +
                            "                        <tel/>\n" +
                            "                        <ref>64101,2016</ref>\n" +
                            "                    </lawyer>\n" +
                            "                    <cedcon/>\n" +
                            "                    <settlement/>\n" +
                            "                    <latest_status/>\n" +
                            "                    <subject_cmt/>\n" +
                            "                    <cra_cmt/>\n" +
                            "                </record>\n" +
                            "            </section_d>\n" +
                            "            <section_d2 title=\"SECTION D2 - SUBJECT AS PLAINTIFF\" data=\"false\"/>\n" +
                            "            <section_d4 title=\"SECTION D4 - NON PERSONAL CAPACITY\" data=\"false\"/>\n" +
                            "            <section_ccris title=\"SECTION CCRIS\" data=\"false\"/>\n" +
                            "            <section_dcheqs title=\"\" data=\"false\"/>\n" +
                            "            <section_ccris_supp title=\"SECTION CCRIS SUPPLEMENTARY\" data=\"false\"/>\n" +
                            "            <section_angkasa title=\"SECTION ANGKASA\" data=\"false\"/>\n" +
                            "            <section_etr_plus title=\"ETR PLUS\" data=\"false\"/>\n" +
                            "            <section_e title=\"SECTION E - TRADE REFEREES\" data=\"false\"/>\n" +
                            "        </enquiry>\n" +
                            "    </enq_report>\n" +
                            "</report>";


            int startIndex = test.indexOf("<enq_status");
            int endIndex = test.indexOf("</enq_status>");
            String purposeString = test.substring(startIndex, endIndex);

            if (purposeString.contains("1")) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(test.getBytes());
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(jsonNode);

//            Map<String, Object> jsonMap = objectMapper.readValue(value, Map.class);
//            Object ob = jsonMap.get("enq_report");
//            String nodeOne = objectMapper.writeValueAsString(ob);
//
//            Map<String, Object> jsonMap2 = objectMapper.readValue(nodeOne, Map.class);
//            Object ob2 = jsonMap2.get("header");
//            String nodeTwo = objectMapper.writeValueAsString(ob2);
//
//
//            Map<String, Object> jsonMap3 = objectMapper.readValue(nodeTwo, Map.class);
//            Object ob3 = jsonMap3.get("enq_status");
//            String nodeThree = objectMapper.writeValueAsString(ob3);
//
//            Map<String, Object> jsonMap4 = objectMapper.readValue(nodeThree, Map.class);
//            Object ob4 = jsonMap4.get("code");
//            String nodeFour = objectMapper.writeValueAsString(ob4);

            LinkedHashMap<String, Object> jsonMap = objectMapper.readValue(value, LinkedHashMap.class);
            String nodeOne = objectMapper.writeValueAsString(jsonMap.get("enq_report"));
            if (null == nodeOne || "".equals(nodeOne)) return;
            String nodeTwo = objectMapper.writeValueAsString(objectMapper.readValue(nodeOne, LinkedHashMap.class).get("header"));
            if (null == nodeTwo || "".equals(nodeTwo)) return;
            String nodeThree = objectMapper.writeValueAsString(objectMapper.readValue(nodeTwo, LinkedHashMap.class).get("enq_status"));
            if (null == nodeThree || "".equals(nodeThree)) return;
            String nodeFour = objectMapper.writeValueAsString(objectMapper.readValue(nodeThree, LinkedHashMap.class).get("code"));
            if (null == nodeFour || "".equals(nodeFour)) return;
//            String nodeOne = objectMapper.writeValueAsString(jsonMap.get("enq_report") != null ? jsonMap.get("enq_report") : new HashMap<>());
//            String nodeTwo = objectMapper.writeValueAsString(objectMapper.readValue(nodeOne, Map.class).get("header") != null ? objectMapper.readValue(nodeOne, Map.class).get("header") : new HashMap<>());
//            String nodeThree = objectMapper.writeValueAsString(objectMapper.readValue(nodeTwo, Map.class).get("enq_status") != null ? objectMapper.readValue(nodeTwo, Map.class).get("enq_status") : new HashMap<>());
//            String nodeFour = objectMapper.writeValueAsString(objectMapper.readValue(nodeThree, Map.class).get("code") != null ? objectMapper.readValue(nodeThree, Map.class).get("code") : new HashMap<>());

            if (nodeFour.length() > 1 && "1".equals(nodeFour.substring(1, nodeFour.length() - 1))) {
                System.out.println(nodeFour);
                System.out.println("________________");
            }
            String rs = String.valueOf(jsonNode.at("/enq_report/header/enq_status/code"));
            System.out.println(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
