package com.example.imigbomonsterwiki;

import android.content.Context;
import android.widget.ImageView;

public class MonsterFilter {
    private ImageView selectedRarity = null;
    private ImageView selectedElement = null;
    private MonsterWikiView monsterWikiView;

    final private int kMonsterDisplaysInitialAmount = 20;
    private int actualTableOffset = kMonsterDisplaysInitialAmount;

    public MonsterFilter(MonsterWikiView monsterWikiView){
        this.monsterWikiView = monsterWikiView;
    }

    protected static final String[] elementsList = {"f","n","e","t","w","d","l","s","mt"};

    protected static int getRarityStroke(byte n){
        int id = 0;
        switch (n){
            case 1:
                id = R.drawable.stroke1;
                break;
            case 2:
                id = R.drawable.stroke2;
                break;
            case 3:
                id = R.drawable.stroke3;
                break;
            case 4:
                id = R.drawable.stroke4;
                break;
            case 5:
                id = R.drawable.stroke5;
                break;
        }
        return id;
    }

    protected static int getElementIcon(String element){
        int elementIcon = 0;

        switch (element){
            case "f":
                elementIcon = R.drawable.f;
                break;
            case "n":
                elementIcon = R.drawable.n;
                break;
            case "e":
                elementIcon = R.drawable.e;
                break;
            case "t":
                elementIcon = R.drawable.t;
                break;
            case "w":
                elementIcon = R.drawable.w;
                break;
            case "d":
                elementIcon = R.drawable.d;
                break;
            case "l":
                elementIcon = R.drawable.l;
                break;
            case "s":
                elementIcon = R.drawable.s;
                break;
            case "mt":
                elementIcon = R.drawable.mt;
                break;
        }

        return elementIcon;
    }

    protected String getSQLCondition(){
        String whereClause = "";

        if(selectedRarity != null || selectedElement != null){
            whereClause = "WHERE ";
            boolean hasAlreadyCondition = false;
            if(selectedRarity != null){
                whereClause += "RARITY = "+selectedRarity.getId()+" ";
                hasAlreadyCondition = true;
            }

            if(selectedElement != null){
                if(hasAlreadyCondition){
                    whereClause += "AND";
                }
                whereClause += " (ELEMENT1 = '"+elementsList[selectedElement.getId()]+"' OR ELEMENT2 = '"+elementsList[selectedElement.getId()]+"') ";
            }
        }

        return whereClause;
    }

    protected void onRaritySelected(ImageView view, Context context){
        //Update FilterView:
        if(selectedRarity == null || selectedRarity.getId() != view.getId()){
            view.setBackground(context.getDrawable(R.drawable.stroke0));

            if(selectedRarity != null){
                selectedRarity.setBackground(null);
            }

            selectedRarity = view;
        }else{
            view.setBackground(null);
            selectedRarity = null;
        }
        updateMonsterWikiView(context);

    }

    protected void onElementSelected(ImageView view, Context context){
        //Update element rarity filter:
        if(selectedElement == null || selectedElement.getId() != view.getId()){
            view.setBackground(context.getDrawable(R.drawable.element0));

            if(selectedElement != null){
                selectedElement.setBackground(null);
            }

            selectedElement = view;
        }else{
            view.setBackground(null);
            selectedElement = null;
        }
        updateMonsterWikiView(context);
    }

    protected void onShowMoreClicked(Context context){
        actualTableOffset += kMonsterDisplaysInitialAmount;
        final String condition = getSQLCondition();
        final MonsterDatabase db = new MonsterDatabase(context, false);
        monsterWikiView.updateMonsterTableView(db.getMonstersDisplayOrderByRelease(kMonsterDisplaysInitialAmount, actualTableOffset, condition), false);

        final int amount = db.getMonstersAmount(condition);

        monsterWikiView.setShowMoreTextViewVisible(amount >= actualTableOffset + kMonsterDisplaysInitialAmount);
    }

    private void updateMonsterWikiView(Context context){
        actualTableOffset = 0;
        final String condition = getSQLCondition();
        final MonsterDatabase db = new MonsterDatabase(context, false);
        monsterWikiView.updateMonsterTableView(db.getMonstersDisplayOrderByRelease(kMonsterDisplaysInitialAmount, 0, condition), true);

        final int amount = db.getMonstersAmount(condition);

        monsterWikiView.setShowMoreTextViewVisible(amount >= kMonsterDisplaysInitialAmount);
    }
}
