package io.github.sspanak.tt9.db.migrations;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Locale;

import io.github.sspanak.tt9.Logger;
import io.github.sspanak.tt9.languages.Language;
import io.github.sspanak.tt9.languages.LanguageCollection;

public class DB11 {
	private Context ctx;

	public Migration getMigration(Context context) {
		ctx = context;
		return MIGRATION;
	}

	private final Migration MIGRATION = new Migration(10, 11) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {
			final String enWords = "'I''d','I''m','d''annunzio','I''ll','I''ve','prud''hon','an''t','bo''s''n','bo''s''ns','bo''sun','bo''suns','bos''n','bos''ns','br''er','ca''canny','could''ve','d''arezzo','d''estaing','e''en','e''er','fo''c''s''le','fo''c''s''les','fo''c''sle','fo''c''sles','ha''penny','he''d','he''ll','how''d','how''re','howe''er','it''d','it''ll','might''ve','must''ve','n''importe','ne''er','nor''easter','nor''wester','o''er','rec''d','sec''y','she''d','she''ll','should''ve','sou''wester','ta''en','that''d','that''ll','they''d','they''ll','they''re','they''ve','we''d','we''ll','we''re','we''ve','whate''er','whatsoe''er','whene''er','where''er','who''d','who''ll','who''re','who''ve','why''d','would''ve','you''d','you''ll','you''re','you''ve','Ch''in','L''Amour','L''Enfant','L''Oreal','L''Ouverture','T''ang','Xi''an'";
			final String nlWords = "'''s-Graveland','''s-Gravendeel','''s-Gravenhaags','''s-Gravenhage','''s-Gravenhagenaar','''s-Gravenmoer','''s-Gravenzande','''s-Gravenzander','''s-Gravenzands','''s-Hertogenbosch','''t','A.D.','az.','chin.','d.v.','h.k.h.','h.m.','l.b.','mgr.','n.b.','n.h.','n.n.','n.o.','n.v.','n.w.','ned.','o.l.v.','openoffice.org','r.i.p.','st.-eustatius','st.-maarten','stct.','w.','w.v.str.','z.h.','z.k.h.','a.d.h.v.','a.g.v.','a.h.w.','a.j.b.','a.m.','a.s.','a.u.b.','aanw.','afb.','afd.','afz.','an.','arr.','b.d.','b.g.g.','b.v.d.','bc.','bett.','bijl.','bijv.','blz.','bv.','bw.','c.q.','c.s.','ca.','d.d.','d.i.','d.m.v.','d.w.z.','dd.','dhr.','div.','dr.','dra.','drs.','drs.-titel','ds.','e.a.','e.d.','e.e.a.','e.o.','e.v.','e.v.a.','enz.','etc.','evt.','excl.','fa.','fam.','fig.','fr.','g.g.d.','geb.','gem.','get.','i.c.','i.c.m.','i.e.','i.h.a.','i.h.b.','i.m.','i.o.','i.o.v.','i.p.v.','i.s.m.','i.t.t.','i.v.m.','i.z.g.st.','incl.','ing.','ir.','jhr.','jkvr.','jl.','jr.','k.k.','lic.','m.','m.a.w.','m.b.t.','m.b.v.','m.i.','m.i.v.','m.m.','m.m.v.','m.n.','m.u.v.','max.','mevr.','min.','mld.','mln.','mr.','mw.','n.a.v.','n.o.t.k.','n.v.t.','nl.','nl.openoffice.org','no.','nr.','nrs.','o.a.','o.b.v.','o.i.','o.i.d.','o.m.','o.t.t.','o.v.t.','o.v.v.','ong.','p.','p.a.','p.m.','p.o.','p.p.','p.w.','pag.','plm.','plv.','prof.','q.e.d.','q.q.','r.-k.','red.','resp.','s.j.','s.v.p.','sr.','t.a.v.','t.b.v.','t.g.v.','t.h.t.','t.h.v.','t.n.v.','t.o.v.','t.w.','t.w.v.','t.z.t.','v.','v.chr.','v.d.','v.h.','v.l.n.r.','v.r.n.l.','v.t.t.','v.v.','v.v.t.','v.w.b.','verg.','vgl.','vnl.','vnw.','voorz.','vs.','w.o.','w.v.t.t.k.','ww.','z.g.','z.g.a.n.','z.i.','z.o.z.','z.s.m.','zgn.'";

			try {
				Language English = LanguageCollection.getByLocale(ctx, Locale.ENGLISH.toString());
				Language Dutch = LanguageCollection.getByLocale(ctx, "nl_NL");
				assert English != null;
				assert Dutch != null;

				database.beginTransaction();
				database.execSQL(getDeleteEnglishSwordsQuery(English));
				database.execSQL(getDeleteWordsQuery(English.getId(), enWords));
				database.execSQL(getDeleteWordsQuery(Dutch.getId(), nlWords));
				database.setTransactionSuccessful();
			} catch (Exception e) {
				Logger.e("Migrate to DB11", "Migration failed. " + e.getMessage());
			} finally {
				database.endTransaction();
			}
		}
	};

	private String getDeleteEnglishSwordsQuery(Language English) {
		return "DELETE FROM words WHERE lang=" + English.getId() + " AND word LIKE '%''s'";
	}

	private String getDeleteWordsQuery(int langId, String wordList) {
		return "DELETE FROM words WHERE lang=" + langId + " AND word IN(" + wordList + ")";
	}
}
