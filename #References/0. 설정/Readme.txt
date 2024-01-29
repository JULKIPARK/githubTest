Azure에서 VM을 구성하고 외부 개발자에게 SSH 접근 권한을 주는 과정은 다음과 같습니다.

VM 생성: Azure 포털에서 새로운 VM(Virtual Machine)을 생성합니다. 이때, SSH 키 페어를 사용하여 로그인 할 수 있도록 설정해야 합니다.
SSH 키 생성: 각 개발자가 자신의 컴퓨터에서 RSA 키 페어를 생성합니다. 이는 ssh-keygen 명령어를 사용하여 수행할 수 있습니다.
bash
ssh-keygen -t rsa -b 4096

이 명령은 새로운 RSA 암호화 키를 생성하며, 기본적으로 ~/.ssh/id_rsa (private key) 및 ~/.ssh/id_rsa.pub (public key) 파일에 저장됩니다.
Public Key 전달: 각 개발자는 자신의 public key (id_rsa.pub 파일의 내용)를 Azure 관리자에게 전달합니다.
SSH Public Key 추가: Azure 관리자는 Azure 포털 또는 Azure CLI(Command Line Interface)를 사용하여 해당 VM의 SSH 설정에 각 개발자의 public key를 추가합니다.
네트워크 보안 그룹 설정: 해당 VM이 속한 네트워크 보안 그룹(Network Security Group, NSG)에서 인바운드 규칙을 수정하거나 추가하여 원격 개발자들이 SSH 포트(기본적으로 22번 포트)로 접근할 수 있도록 합니다.
접속 정보 제공: 완료되면, 외부 개발자들에게 VM의 공용 IP 주소와 사용할 사용자 이름을 알려줍니다. 그러면 각각의 private key (id_rsa) 파일을 가지고 있는 외부 개발자들은 다음과 같은 명령어로 SSH 접속이 가능해집니다.

bash
ssh username@vm_public_ip_address
주요 사항:

모든 접근 가능한 IP 주소 대신 필요한 IP 주소만 NSG에 추가하는 것이 좋습니다.
필요한 경우, 별도의 사용자 계정을 만들고 sudo 권한 등 필요한 권한만 부여하는 것이 좋습니다.
Private Key는 반드시 안전하게 보관해야 하며, 절대 외부에 노출되어서는 안됩니다.
VM의 SSH 설정을 변경하려면 관리자 권한이 필요합니다.



ftpuser
xGldzvurKR5/dbiG26arTAb5hhUXDZ3r
https://samilesg.blob.core.windows.net/temp
samilesg.ftpuser@samilesg.blob.core.windows.net


[ 메타원 설정 ]
개발정보
----
SVN
http://121.183.232.72:1180/svn/pwcesg_repo
cxdall / 1q2w3e

---
내부개발서버
1.퍼블FO
http://121.183.232.72:90/frontoffice/html/index.html

2.퍼블BO
http://121.183.232.72:90/backoffice/html/index.html

3.개발FO
http://121.183.232.72:8080/frontoffice

4.개발BO
http://121.183.232.72:8080/backoffice/common/login

---
메일, GIT(또는 SVN) 공유대상
여인환 meta1it@meta1it.com
김남중 bishop74@gmail.com
이준엽 codej101022@gmail.com 010-4744-0041