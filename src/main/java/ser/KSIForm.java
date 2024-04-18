package ser;

import com.ser.blueline.*;
import com.ser.blueline.bpm.IProcessInstance;
import com.ser.blueline.bpm.ITask;
import com.ser.blueline.metaDataComponents.IArchiveClass;
import de.ser.doxis4.agentserver.UnifiedAgent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class KSIForm extends UnifiedAgent {
    private Logger log = LogManager.getLogger();
    private ProcessHelper helper;
    private IDocument mainDocument;
    JSONObject projects = new JSONObject();
    private ITask mainTask;
    private String notes;
    List<String> exportedFiles = new ArrayList<>();
    @Override
    protected Object execute() {
        helper = new ProcessHelper(getSes());
        mainTask = getEventTask().findParentTask();
        IProcessInstance processInstance = mainTask.getProcessInstance();
        String currentUser = getSes().getUser().getFullName();
        IUser processOwner = processInstance.getOwner();
        String uniqueId = UUID.randomUUID().toString();

        if (mainTask == null) return resultError("OBJECT CLIENT ID is NULL or not of Type ITask");
        try {
            com.spire.license.LicenseProvider.setLicenseKey(Conf.Licences.SPIRE_XLS);

            Utils.session = getSes();
            Utils.bpm = getBpm();
            Utils.server = Utils.session.getDocumentServer();
            Utils.loadDirectory(Conf.GenerateKSI.MainPath);

            //helper = new ProcessHelper(getSes());
            log.info("----KSIForm Process Agent Started -----:" + mainTask.getID());
            notes = mainTask.getDescriptorValue("Notes");
            if (mainTask.getProcessInstance().findLockInfo().getOwnerID() != null) {
                log.error("Task is locked.." + mainTask.getID() + "..restarting agent");
                return resultRestart("Restarting Agent");
            }

            int cntA = 0;
            int cntB = 0;
            IInformationObjectLinks links = mainTask.getProcessInstance().getLoadedInformationObjectLinks();
            for (ILink link : links.getLinks()) {
                IInformationObject xdoc = link.getTargetInformationObject();
                String type = xdoc.getDescriptorValue("ObjectType");
                if (!xdoc.getClassID().equals(Conf.ClassIDs.PladisDoc)) {
                    continue;
                }
                if(Objects.equals(type, "AFTER")){
                    cntA++;
                    Utils.exportDocument((IDocument) xdoc,Conf.Paths.KSIMainPath,"KSIPicture_" + uniqueId + "_" + type + "_" + cntA);///"KSIPicture_abdf12323_BEFORE_1.png"
                    exportedFiles.add(Conf.Paths.KSIMainPath + "/" + "KSIPicture_" + uniqueId + "_" + type + "_" + cntA + ".png");
                }
                if(Objects.equals(type, "BEFORE")){
                    cntB++;
                    Utils.exportDocument((IDocument) xdoc,Conf.Paths.KSIMainPath,"KSIPicture_" + uniqueId + "_" + type + "_" + cntB);///"KSIPicture_abdf12323_BEFORE_1.png"
                    exportedFiles.add(Conf.Paths.KSIMainPath + "/" + "KSIPicture_" + uniqueId + "_" + type + "_" + cntB + ".png");
                }
            }
            String mtpn = "KSI_FORM_TEMPLATE";

           /*  projects = Utils.getProjectWorkspaces(helper);
            IDocument mtpl = null;
            for (String prjn : projects.keySet()) {
                IInformationObject prjt = (IInformationObject) projects.get(prjn);
                IDocument dtpl = Utils.getTemplateDocument(prjt, mtpn);
                if (dtpl == null) {
                    continue;
                }
                mtpl = dtpl;
                break;
            }*/
            JSONObject dbks = new JSONObject();
            dbks.put("FormNo", "KSI0001");
            dbks.put("RevisionNo", "1");
            dbks.put("Date", "");
            dbks.put("OrgDepartment", mainTask.getDescriptorValue("OrgDepartment"));
            dbks.put("ObjectAuthors", mainTask.getDescriptorValue("ObjectAuthors").replace("[", "").replace("]", ""));
            dbks.put("Problem", mainTask.getDescriptorValue("Problem"));
            dbks.put("Solution", mainTask.getDescriptorValue("Solution"));
            dbks.put("Earning", mainTask.getDescriptorValue("Earning"));

            dbks.put("ProblemTurleri", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri2", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri2"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri3", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri3"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri4", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri4"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri5", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri5"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri6", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri6"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri7", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri7"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri8", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri8"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri9", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri9"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri10", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri10"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri11", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri11"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri12", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri12"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri13", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri13"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri14", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri14"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri15", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri15"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri16", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri16"), "False") ? "X" : ""));
            dbks.put("ProblemTurleri17", (!Objects.equals(mainTask.getDescriptorValue("ProblemTurleri17"), "False") ? "X" : ""));

            //String tplMailPath = Utils.exportDocument(mtpl, Conf.GenerateKSI.MainPath, mtpn + "[" + uniqueId + "]");
            String tplMailPath = Conf.GenerateKSI.MainTemplatePath + "/" + mtpn + ".xlsx";
            String mailExcelPath = Utils.saveDocExcel(
                    tplMailPath,
                    0,
                    Conf.GenerateKSI.MainPath + "/" + mtpn + "[" + uniqueId + "].xlsx",
                    dbks,
                    exportedFiles,
                    "KSI"
            );
            String mailHtmlPath = Utils.convertExcelToHtml(mailExcelPath,
                    0,
                    Conf.GenerateKSI.MainPath + "/" + mtpn + "[" + uniqueId + "].html");

            String mailPdfPath = Utils.convertExcelToPdf(mailExcelPath, Conf.GenerateKSI.MainPath + "/" + mtpn + "[" + uniqueId + "].pdf");

            this.archiveNewTemplate(mailPdfPath);
            /*String umail = processOwner.getEMailAddress();
            List<String> mails = new ArrayList<>();
            log.info("Mail To : " + String.join(";", mails));
            if (umail != null) {
                mails.add(umail);
                JSONObject mail = new JSONObject();
                mail.put("To", String.join(";", mails));
                mail.put("Subject", "Deleted Documents");
                mail.put("BodyHTMLFile", mailHtmlPath);
                Utils.sendHTMLMail(mail, null);
            } else {
                log.info("Mail adress is null :" + processOwner.getFullName());
            }*/
            log.info("----KSIForm Process Agent Finished -----");

        } catch (Exception e) {
            log.error("Exception Caught");
            log.error(e.getMessage());
            return resultError(e.getMessage());
        }

        return resultSuccess("Agent Finished Succesfully");
    }
    private void archiveNewTemplate(String tpltSavePath) throws Exception {
        IDocument doc = newFileToDocumentClass(tpltSavePath, Conf.ClassIDs.PladisDoc);
        doc.setDescriptorValue("OrgCompanyDescription" , "PLADIS");
        doc.setDescriptorValue("ObjectType" , "GENERAL");
        doc.commit();
        getEventTask().getProcessInstance().getLoadedInformationObjectLinks().addInformationObject(doc.getID());
        getEventTask().commit();
    }
    public IDocument newFileToDocumentClass(String filePath, String archiveClassID) throws Exception {
        IArchiveClass cls = Utils.server.getArchiveClass(archiveClassID, Utils.session);
        if (cls == null) cls = Utils.server.getArchiveClassByName(Utils.session, archiveClassID);
        if (cls == null) throw new Exception("Document Class: " + archiveClassID + " not found");

        String dbName = Utils.session.getDatabase(cls.getDefaultDatabaseID()).getDatabaseName();
        IDocument doc = Utils.server.getClassFactory().getDocumentInstance(dbName, cls.getID(), "0000", Utils.session);

        File file = new File(filePath);
        IRepresentation representation = doc.addRepresentation(".pdf" , "Signed document");
        IDocumentPart newDocumentPart = representation.addPartDocument(filePath);
        doc.commit();
        return doc;
    }
}
