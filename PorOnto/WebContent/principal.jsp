<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<html>
<head>
<title>Ferramenta</title>
<style>
.list {
	background-color: white;
}

.list_disabled {
	background-color: #f1f1f1;
}

.entry {
	color: Brown;
	font-weight: bold;
}

.entry_disabled {
	background-color: #f1f1f1;
	color: gray;
}

.entry_control {
	text-decoration: none;
	color: Brown;
	font-weight: bold;
}

.entry_control_disabled {
	background-color: #f1f1f1;
	color: #BCDEFF;
	text-decoration: none;
	color: gray;
}
</style>
</head>
<body>
<f:view>
	<a4j:form>
		<rich:toolBar>
			<rich:dropDownMenu value="Arquivo">
				<rich:menuItem submitMode="ajax" value="New"
					action="#{principalBean.doNovo}" reRender="tb" />
				<rich:menuItem submitMode="ajax" value="Gerar Ontologia"
					action="#{principalBean.doOnto}" reRender="ontoTab" />
				<rich:menuGroup value="Exportar para...">
					<rich:menuItem submitMode="ajax">
						<h:commandLink action="#{principalBean.doExcel}">
							<h:outputText value="Excel" />
						</h:commandLink>
					</rich:menuItem>
					<rich:menuItem submitMode="ajax" value="OWL"
						action="#{principalBean.doOwl}" />
				</rich:menuGroup>
			</rich:dropDownMenu>
		</rich:toolBar>
		<rich:tabPanel switchType="ajax"
			selectedTab="#{principalBean.selectedTab}" id="tb">
			<rich:tab label="Configuração" id="config">
				<h:panelGrid border="0" columns="2">
					<h:outputLabel value="Minimo" for="minimo" styleClass="rich-input" />
					<h:inputText id="minimo" styleClass="rich-input"
						value="#{principalBean.minimo}" size="4" />
					<h:outputLabel value="Minimo Termos Compostos" for="minimotc"
						styleClass="rich-input" />
					<h:inputText id="minimotc" styleClass="rich-input"
						value="#{principalBean.minimotc}" size="4" />
					<h:outputLabel value="Termos Compostos" for="termosCompostos"
						styleClass="rich-input" />
					<h:selectBooleanCheckbox id="termosCompostos"
						value="#{principalBean.termosCompostos}" styleClass="rich-input" />
					<h:outputLabel value="Apenas Substantivos" for="apenasSubstantivos"
						styleClass="rich-input" />
					<h:selectBooleanCheckbox id="apenasSubstantivos"
						value="#{principalBean.apenasSubstantivos}"
						styleClass="rich-input" />
					<h:outputLabel value="Tfidf" for="tfidf" styleClass="rich-input" />
					<h:selectBooleanCheckbox id="tfidf" value="#{principalBean.tfidf}"
						styleClass="rich-input" />
					<h:outputLabel value="Entropy" for="entropy"
						styleClass="rich-input" />
					<h:selectBooleanCheckbox id="entropy"
						value="#{principalBean.entropy}" styleClass="rich-input" />
					<h:outputLabel value="Total de Palavras (Repetidas)"
						styleClass="rich-input" />
					<h:outputLabel id="totalDePalavrasComRepetidas"
						value="#{principalBean.totalDePalavrasComRepetidas}"
						styleClass="rich-input" />
					<h:outputLabel value="Total de Palavras (Únicas)"
						styleClass="rich-input" />
					<h:outputLabel id="totalDePalavrasSemRepetidas"
						value="#{principalBean.totalDePalavrasSemRepetidas}"
						styleClass="rich-input" />
					<h:outputLabel value="Total de Palavras (Selecionadas)"
						styleClass="rich-input" />
					<h:outputLabel id="totalDePalavrasSelecionadas"
						value="#{principalBean.totalDePalavrasSelecionadas}"
						styleClass="rich-input" />
					<a4j:commandButton id="botaoGerar" action="#{principalBean.gerarLista}"
						value="gerar"
						reRender="dt,tb,totalDePalavrasComRepetidas,totalDePalavrasSemRepetidas,totalDePalavrasSelecionadas"
						styleClass="rich-button" disabled="#{principalBean.gerar}">
					</a4j:commandButton>
					<rich:progressBar value="#{principalBean.incValue}" />
				</h:panelGrid>
				<br>
				<rich:fileUpload id="upload1"
					fileUploadListener="#{principalBean.upload}" listWidth="500"
					listHeight="210" maxFilesQuantity="50" acceptedTypes="pdf,txt"
					allowFlash="auto" addControlLabel="Incluir"
					cancelEntryControlLabel="Cancelar" noDuplicate="true"
					stopControlLabel="Parar" doneLabel="Concluido"
					stopEntryControlLabel="Parar" clearAllControlLabel="Limpar Tudo"
					clearControlLabel="Limpar" required="true" requiredMessage="Favor enviar um arquivo">
					<a4j:support event="onuploadcomplete" reRender="files_list,botaoGerar" />
					<f:facet name="label">
						<h:outputText value="{_KB}KB from {KB}KB uploaded --- {mm}:{ss}"></h:outputText>
					</f:facet>
				</rich:fileUpload>
				<rich:dataTable value="#{principalBean.itens}" var="file"
					id="files_list" style="width: 500px" align="top">
					<f:facet name="header">
						<h:outputText value="Arquivos Enviados" />
					</f:facet>
					<rich:column>
						<h:outputText value="#{file.fileName}"></h:outputText>
					</rich:column>
				</rich:dataTable>
				<br>

			</rich:tab>
			<rich:tab label="Resultados" id="result">
				<rich:panelBar>
					<rich:panelBarItem label="Todos os Termos">
						<rich:dataTable id="dt" value="#{principalBean.valores}"
							var="valor" width="600px" columnClasses="center" rows="20"
							reRender="ds" align="center">
							<f:facet name="header">
								<h:outputText value="Resultados" />
							</f:facet>
							<rich:column style="text-align:center">
								<f:facet name="header">
									<h:selectBooleanCheckbox value="#{principalBean.blSelectTodos}"
										id="blSelectTodos">
										<a4j:support event="onchange"
											action="#{principalBean.selectTodos}" reRender="dt"></a4j:support>
									</h:selectBooleanCheckbox>
								</f:facet>
								<h:selectBooleanCheckbox value="#{valor.selected}"
									id="checkTodos">
									<a4j:support event="onchange">
										<f:setPropertyActionListener value="#{valor.selected}" target="#{valor.selected}" />
									</a4j:support>
								</h:selectBooleanCheckbox>
							</rich:column>
							<rich:column sortBy="#{valor.lemma}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Lemma" />
								</f:facet>
								<h:outputText value="#{valor.lemma}" />
							</rich:column>
							<rich:column sortBy="#{valor.total}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Total" />
								</f:facet>
								<h:outputText value="#{valor.total}" />
							</rich:column>
							<rich:column sortBy="#{valor.tfidf}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Tfidf" />
								</f:facet>
								<h:outputText value="#{valor.tfidf}" />
							</rich:column>
							<rich:column sortBy="#{valor.entropy}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Entropy" />
								</f:facet>
								<h:outputText value="#{valor.entropy}" />
							</rich:column>
							<rich:column sortBy="#{valor.decs}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Decs" />
								</f:facet>
								<h:graphicImage value="/img/x.png" rendered="#{!valor.decs}" />
								<h:graphicImage value="/img/v.png" rendered="#{valor.decs}" />
							</rich:column>
							<rich:column sortBy="#{valor.sinonimos}" style="text-align:left"
								width="100px">
								<f:facet name="header">
									<h:outputText value="Sinônimos" />
								</f:facet>
								<h:outputText value="#{valor.sinonimoToolTip}"
									rendered="#{valor.sinonimosB}" />
								<rich:toolTip rendered="#{valor.sinonimosB}">
									<span style="white-space: nowrap"> <rich:dataList
										value="#{valor.sinonimos}" var="sin">
										<h:outputText value="#{sin}" />
									</rich:dataList> </span>
								</rich:toolTip>
							</rich:column>
							<f:facet name="footer">
								<rich:datascroller id="ds"></rich:datascroller>
							</f:facet>
						</rich:dataTable>
					</rich:panelBarItem>
					<rich:panelBarItem label="Apenas Termos Compostos">
						<rich:dataTable id="dt2" value="#{principalBean.valoresCompostos}"
							var="valor" width="600px" columnClasses="center" rows="20"
							reRender="ds" align="center">
							<f:facet name="header">
								<h:outputText value="Resultados" />
							</f:facet>
							<rich:column style="text-align:center">
								<f:facet name="header">
									<h:selectBooleanCheckbox value="#{principalBean.blSelectTC}"
										id="blSelectTC">
										<a4j:support event="onchange"
											action="#{principalBean.selectTC}" reRender="dt2"></a4j:support>
									</h:selectBooleanCheckbox>
								</f:facet>
								<h:selectBooleanCheckbox value="#{valor.selected}"
									id="checkTC">
									<a4j:support event="onchange">
										<f:setPropertyActionListener value="#{valor.selected}" target="#{valor.selected}" />
									</a4j:support>
								</h:selectBooleanCheckbox>
							</rich:column>
							<rich:column sortBy="#{valor.lemma}">
								<f:facet name="header">
									<h:outputText value="Lemma" />
								</f:facet>
								<h:outputText value="#{valor.lemma}" />
							</rich:column>
							<rich:column sortBy="#{valor.total}">
								<f:facet name="header">
									<h:outputText value="Total" />
								</f:facet>
								<h:outputText value="#{valor.total}" />
							</rich:column>
							<rich:column sortBy="#{valor.tfidf}">
								<f:facet name="header">
									<h:outputText value="Tfidf" />
								</f:facet>
								<h:outputText value="#{valor.tfidf}" />
							</rich:column>
							<rich:column sortBy="#{valor.entropy}">
								<f:facet name="header">
									<h:outputText value="Entropy" />
								</f:facet>
								<h:outputText value="#{valor.entropy}" />
							</rich:column>
							<rich:column sortBy="#{valor.decs}" style="text-align:center">
								<f:facet name="header">
									<h:outputText value="Decs" />
								</f:facet>
								<h:graphicImage value="/img/x.png" rendered="#{!valor.decs}" />
								<h:graphicImage value="/img/v.png" rendered="#{valor.decs}" />
							</rich:column>
							<rich:column sortBy="#{valor.tipoComposto}">
								<f:facet name="header">
									<h:outputText value="Tipo Composto" />
								</f:facet>
								<h:outputText value="#{valor.tipoComposto}" />
							</rich:column>
							<rich:column sortBy="#{valor.sinonimos}" style="text-align:left"
								width="100px">
								<f:facet name="header">
									<h:outputText value="Sinônimos" />
								</f:facet>
								<h:outputText value="#{valor.sinonimoToolTip}"
									rendered="#{valor.sinonimosB}" />
								<rich:toolTip rendered="#{valor.sinonimosB}">
									<span style="white-space: nowrap"> <rich:dataList
										value="#{valor.sinonimos}" var="sin">
										<h:outputText value="#{sin}" />
									</rich:dataList> </span>
								</rich:toolTip>
							</rich:column>
							<f:facet name="footer">
								<rich:datascroller id="ds2"></rich:datascroller>
							</f:facet>
						</rich:dataTable>
					</rich:panelBarItem>
				</rich:panelBar>
			</rich:tab>
			<rich:tab label="Ontologia" id="ontoTab">
				<rich:tree style="width:300px" value="#{principalBean.rootNode}"
					var="valor">
					<rich:treeNode>
						<h:outputText value="#{valor}" />
					</rich:treeNode>
				</rich:tree>
			</rich:tab>
		</rich:tabPanel>
	</a4j:form>
</f:view>
</body>
</html>
