package demo.main;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;


/**
 * ��������ʹ��shiro api
 *
 */
public class Quickstart {

    private static final transient Logger log = Logger.getLogger(Quickstart.class);


    public static void main(String[] args) {

    	//1.��shiro.ini���صõ�securityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-main.ini");
        SecurityManager securityManager = factory.getInstance();

        //2.���õ�SecurityUtils�У�ʵ�ֵ���
        SecurityUtils.setSecurityManager(securityManager);

        //..shiro�Ļ��������

        // �õ��������һ����������û�user
        Subject currentUser = SecurityUtils.getSubject();

        // 3.��һЩsession����������Ҫweb������shiro api�д���Session����
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("��session��ȡ��someKey��ֵΪ:"+value);
        }
        boolean login = true;
        // 4.��½�û�������ɽ�ɫ��Ȩ�޵���֤
        if (!currentUser.isAuthenticated()) {//�����ǰ����û�е�½
            //4.1.��ȡ�û���¼Ʊ��
        	UsernamePasswordToken token = new UsernamePasswordToken("root", "secret");//ʹ��lonestarrΪloginname,vespaΪpwd����½
            //4.2.��¼�û�Ʊ��
        	token.setRememberMe(true);
            try {
            	//4.3.��½�û�
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("�û��������� -- " + token.getPrincipal());
                login = false;
            } catch (IncorrectCredentialsException ice) {
                log.info("�˻����� " + token.getPrincipal() + " ����ȷ��");
                login = false;
            } catch (LockedAccountException lae) {
                log.info("�˻� " + token.getPrincipal() + " ������  " +
                        "����ϵ����Ա������");
                login = false;
            }
            catch (AuthenticationException ae) {
            	login = false;
            }
        }
        if(!login){
        	//��½ʧ�ܣ��Ƴ�ϵͳ
        	System.exit(1);
        }
        //4.3.û���쳣�����½�ɹ�
        log.info("�û� [" + currentUser.getPrincipal() + "] ��¼�ɹ���");

        //5.��ɫ��֤����֤����schwartz��ɫ
        if (currentUser.hasRole("schwartz")) {
            log.info("�û�["+currentUser.getPrincipal()+"]ӵ��schwartz��ɫ��");
        } else {
            log.info("�û�["+currentUser.getPrincipal()+"]û��schwartz��ɫ��");
        }

        //6.Ȩ����֤����֤����lightsaber��Դ��weildȨ��
        if (currentUser.isPermitted("lightsaber:weild")) {
            log.info("�û�["+currentUser.getPrincipal()+"]ӵ��lightsaber��Դ��weildȨ�ޣ�");
        } else {
            log.info("�Բ���lightsaber��Դֻ����schwartz��ɫ�С�");
        }

        //6.1.���Ȩ����֤
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("�û�["+currentUser.getPrincipal()+"]ӵ��winnebago:drive:eagle5Ȩ�ޣ�");
        } else {
            log.info("�Բ����㲻������winnebago:drive:eagle5��");
        }

        //7.��ɲ�����ע��
        currentUser.logout();

        System.exit(0);
    }
}
