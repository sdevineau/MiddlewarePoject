package fr.emn.examination.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import fr.emn.examination.model.examen.Case;
import fr.emn.examination.model.examen.Caseacocher;
import fr.emn.examination.model.examen.ChoixMultiples;
import fr.emn.examination.model.examen.CodeOuTexte;
import fr.emn.examination.model.examen.Destination;
import fr.emn.examination.model.examen.EnTete;
import fr.emn.examination.model.examen.Enonce;
import fr.emn.examination.model.examen.Examen;
import fr.emn.examination.model.examen.Exercice;
import fr.emn.examination.model.examen.Information;
import fr.emn.examination.model.examen.Protocole;
import fr.emn.examination.model.examen.Question;

public class MockExam {

	public Examen createMockExam() {
		// Creation of enTete
		String titre = "Titre de l'examen";
		String objet = "objet";
		String date = "18 Décembre 2012";
		String auteur = "Les GSI 2013";
		String source = "source";
		EnTete enTete = new EnTete(titre, objet, date, auteur, source);

		// Creation of the destination
		String uv = "uv";
		String module = "module";
		String coefficient = "coefficient";
		Destination destination = new Destination(uv, module, coefficient);

		// Creation of the version
		Information information = new fr.emn.examination.model.examen.Information(
				enTete, destination, "version", "commentaires");

		// Creation of the exercices

		String title = "titre question1";
		BigInteger id = new BigInteger("10");
		String langage = "francais";
		String introduction = "introduction question";

		BigInteger coeff = new BigInteger("30");
		Exercice exo1 = new Exercice(introduction, createQuestions(), title,
				id, langage, coeff);

		List<Exercice> exercices = new ArrayList<Exercice>();
		exercices.add(exo1);
		Enonce enonce = new Enonce(exercices);

		Examen mockExam = new fr.emn.examination.model.examen.Examen(
				information, "preambule", "introduction", enonce, "conclusion");
		return mockExam;

	}

	private Caseacocher createCaseACaseacocher() {
		String value1 = "choix 1";
		BigInteger id1 = new BigInteger("1");
		boolean correction1 = true;
		Case case1 = new Case(value1, id1, correction1);

		String value2 = "choix 2";
		BigInteger id2 = new BigInteger("2");
		boolean correction2 = true;
		Case case2 = new Case(value2, id2, correction2);

		List<Case> _case = new ArrayList<Case>();
		_case.add(case1);
		_case.add(case2);
		return new Caseacocher(_case);

	}

	private List<Question> createQuestions() {
		//Question 1
		BigInteger id1 = new BigInteger("20");
		String consigne1 = "Dans cette question Q1, vous devez implémenter une belle page HTML";
		String titleQ1 = "Titre de la question Q1";
		CodeOuTexte codeOuTexte1;
		Caseacocher caseacocher1;
		Protocole protocole1 = new Protocole("value", "3 essais", "1.0",
				"20 min", "18 points");
		Question q1 = new Question(consigne1, null, createCaseACaseacocher(),
				null, protocole1, id1, titleQ1);
		//Question 2
		BigInteger id2 = new BigInteger("40");
		String consigne2 = "Dans cette question Q2, vous devez vérifier que ça marche";
		String titleQ2 = "Titre de la question Q2";
		CodeOuTexte codeOuTexte2;
		Caseacocher caseacocher2;
		Protocole protocole2 = new Protocole("value", "2 essais", "1.0",
				"15 min", "5 points");
		Question q2 = new Question(consigne2, null, createCaseACaseacocher(),
				null, protocole2, id2, titleQ2);

		List<Question> questions = new ArrayList<Question>();
		questions.add(q1);
		questions.add(q2);
		return questions;
	}
}
